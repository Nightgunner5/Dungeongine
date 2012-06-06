package dungeongine.bootstrap.base;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.Mongo;
import dungeongine.Main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class Database {
	private Database() {
	}

    public static DB getDB() {
        try {
            Mongo mongo = new Mongo(InetAddress.getLoopbackAddress().getHostAddress(), Main.DB_PORT);
            return mongo.getDB("dungeongine");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

	private static void startup() {
		if (!ownProcess)
			return;
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

	private static final boolean ownProcess;

    private static final List<Thread> callbacks = Lists.newArrayList();

    public static void addShutdownCallback(Thread callback) {
        callbacks.add(callback);
    }

    static {
		boolean own;
		try {
			new Socket(InetAddress.getLoopbackAddress(), Main.DB_PORT).close();
			own = false;
		} catch (IOException ex) {
			own = true;
		}
		ownProcess = own;
        startup();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                for (Thread callback : callbacks) {
                    callback.start();
                }
                for (Thread callback : callbacks) {
                    try {
                        callback.join();
                    } catch (InterruptedException ex) {
                    }
                }
                shutdown();
            }
        });
	}

	private static boolean shutdown() {
		if (!ownProcess)
			return true;
		try {
			new Mongo(InetAddress.getLoopbackAddress().getHostAddress(), Main.DB_PORT).getDB("admin").command(new BasicDBObject().append("shutdown", 1));
			return true;
		} catch (RuntimeException | IOException ex) {
			return false;
		} finally {
			File lock = new File("dungeongine.sav", "mongod.lock");
			if (lock.exists() && !lock.delete())
				Logger.getLogger(Database.class.getName()).warning("Unable to remove mongodb lock.");
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
			if (!archive.getParentFile().isDirectory() && !archive.getParentFile().mkdirs())
				Logger.getLogger(Database.class.getName()).warning("Could not make database folder.");
			URL url = new URL(detectURL());
			ReadableByteChannel in = Channels.newChannel(url.openStream());
			FileChannel out = new FileOutputStream(archive).getChannel();
			try {
				out.transferFrom(in, 0, 1L << 48L);
			} finally {
				in.close();
				out.close();
			}
		}
		if (archive.getName().endsWith(".tgz")) {
			try {
				Runtime.getRuntime().exec(new String[]{"tar", "xzvf", archive.getName()}, null, archive.getParentFile()).waitFor();
			} catch (InterruptedException ex) {
				throw new RuntimeException(ex);
			}
			if (!new File("dungeongine.sav", "bin").exists() && !new File(new File("dungeongine.sav", archive.getName().replace(".tgz", "")), "bin").renameTo(new File("dungeongine.sav", "bin")))
				Logger.getLogger(Database.class.getName()).warning("Could not create database executable.");
			new File("dungeongine.sav/bin/mongod").setExecutable(true);
		} else {
			if (!new File("dungeongine.sav", "bin").isDirectory() && !new File("dungeongine.sav", "bin").mkdirs())
				Logger.getLogger(Database.class.getName()).warning("Could not create database executable.");
			ZipFile file = new ZipFile(archive);
			try {
				Enumeration<? extends ZipEntry> entries = file.entries();
				for (ZipEntry entry; entries.hasMoreElements(); ) {
					entry = entries.nextElement();
					if (entry.getName().replace(archive.getName().replace(".zip", ""), "").substring(1).startsWith("bin")) {
						ReadableByteChannel in = Channels.newChannel(file.getInputStream(entry));
						FileChannel out = new FileOutputStream(new File("dungeongine.sav/bin", new File(entry.getName()).getName())).getChannel();
						try {
							out.transferFrom(in, 0, 1L << 48L);
						} finally {
							in.close();
							out.close();
						}
					}
				}
			} finally {
				file.close();
			}
		}
	}
}
