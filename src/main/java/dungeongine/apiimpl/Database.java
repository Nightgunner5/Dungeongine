package dungeongine.apiimpl;

import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
import dungeongine.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

final class Database {
	private Database() {
	}

	static void startup() {
		shutdown();
		try {
			maybeDownload();
			Runtime.getRuntime().exec(new String[]{File.separatorChar == '/' ? new File("dungeongine.sav/bin", "mongod").getAbsolutePath() : new File("dungeongine.sav/bin", "mongod.exe").getAbsolutePath(),
					"--port", Integer.toString(Main.DB_PORT),
					"--dbpath", new File("dungeongine.sav").getAbsolutePath(),
					"--nohttpinterface", "--smallfiles", "--nojournal", "--noauth"},
					null, new File("dungeongine.sav", "bin").getAbsoluteFile());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	static boolean shutdown() {
		try {
			new Mongo(InetAddress.getLoopbackAddress().getHostAddress(), Main.DB_PORT).getDB("admin").command(new BasicDBObject().append("shutdown", 1));
			return true;
		} catch (Exception ex) {
			return false;
		} finally {
			new File("dungeongine.sav/mongod.lock").delete();
		}
	}

	private static String detectURL() {
		String arch = System.getProperty("os.arch");
		boolean amd64 = arch.equals("i686_64") || arch.equals("x86_64") || arch.equals("amd64");

		String os = System.getProperty("os.name");
		if (os.equals("Linux")) {
			return String.format("http://fastdl.mongodb.org/linux/mongodb-linux-%s-2.0.5.tgz", amd64 ? "x86_64" : "i686");
		} else if (os.startsWith("Windows")) {
			return String.format("http://fastdl.mongodb.org/win32/mongodb-win32-%s-2.0.5.zip", amd64 ? "x86_64" : "i386");
		} else if (os.equals("Mac OS X")) {
			return String.format("http://fastdl.mongodb.org/osx/mongodb-osx-%s-2.0.5.tgz", amd64 ? "x86_64" : "i386");
		} else throw new IllegalStateException("Could not detect platform for database!");
	}

	private static void maybeDownload() throws IOException {
		File archive = new File("dungeongine.sav", new File(detectURL()).getName());
		if (!archive.exists()) {
			archive.getParentFile().mkdirs();
			URL url = new URL(detectURL());
			ReadableByteChannel in = Channels.newChannel(url.openStream());
			FileChannel out = new FileOutputStream(archive).getChannel();
			out.transferFrom(in, 0, 1L << 48L);
			in.close();
			out.close();
		}
		if (archive.getName().endsWith(".tgz")) {
			try {
				Runtime.getRuntime().exec(new String[]{"tar", "xzvf", archive.getName()}, null, archive.getParentFile()).waitFor();
			} catch (InterruptedException ex) {
				throw new RuntimeException(ex);
			}
			new File(new File("dungeongine.sav", archive.getName().replace(".tgz", "")), "bin").renameTo(new File("dungeongine.sav/bin"));
			new File("dungeongine.sav/bin/mongod").setExecutable(true);
		} else {
			new File("dungeongine.sav/bin").mkdirs();
			ZipFile file = new ZipFile(archive);
			Enumeration<? extends ZipEntry> entries = file.entries();
			for (ZipEntry entry; entries.hasMoreElements();) {
				entry = entries.nextElement();
				if (entry.getName().replace(archive.getName().replace(".zip", ""), "").substring(1).startsWith("bin")) {
					ReadableByteChannel in = Channels.newChannel(file.getInputStream(entry));
					FileChannel out = new FileOutputStream(new File("dungeongine.sav/bin", new File(entry.getName()).getName())).getChannel();
					out.transferFrom(in, 0, 1L << 48L);
					in.close();
					out.close();
				}
			}
		}
	}
}
