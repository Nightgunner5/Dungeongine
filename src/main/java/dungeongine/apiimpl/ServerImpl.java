package dungeongine.apiimpl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import dungeongine.api.Plugin;
import dungeongine.api.Server;
import dungeongine.api.entity.Player;
import dungeongine.api.map.World;
import dungeongine.apiimpl.entity.PlayerImpl;
import dungeongine.net.Connection;
import dungeongine.net.packet.Packet02Chat;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerImpl implements Server {
	@Override
	public Player[] getOnlinePlayers() {
		Player[] players = new Player[dungeongine.server.Server.clientMap.size()];
		int i = 0;
		for (Connection connection : dungeongine.server.Server.clientMap.values()) {
			players[i++] = PlayerImpl.get(connection);
		}
		return players;
	}

	@Override
	public void broadcastChatMessage(String message) {
		dungeongine.server.Server.broadcast(new Packet02Chat(message));
	}

	@Override
	public World getWorld(String name) {
		return null;
	}

	private final Map<String, String> availablePlugins = Maps.newConcurrentMap();
	private final Map<String, Plugin> loadedPlugins = Maps.newConcurrentMap();

	@Override
	public void reloadPlugins() {
		for (Plugin plugin : loadedPlugins.values()) {
			plugin.shutdown();
		}
		Set<String> wasLoaded = Sets.newHashSet();
		wasLoaded.addAll(loadedPlugins.keySet());
		loadedPlugins.clear();
		availablePlugins.clear();
		File pluginDir = new File("plugins");
		pluginDir.mkdirs();
		for (File pluginFile : pluginDir.listFiles()) {
			if (pluginFile.getName().endsWith(".jar")) {
				try {
					JarFile pluginJar = new JarFile(pluginFile);
					for (Object o : new Yaml().loadAll(pluginJar.getInputStream(pluginJar.getJarEntry("plugin.yml")))) {
						if (o instanceof Map<?, ?>) {
							Map<String, String> map = (Map<String, String>) o;
							for (Map.Entry<String, String> entry : map.entrySet()) {
								if (availablePlugins.containsKey(entry.getKey()))
									throw new IOException("Failed to load plugin.yml");
								availablePlugins.put(entry.getKey(),  entry.getValue());
							}
						}
					}
				} catch (IOException ex) {
					Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, "Could not open plugin jar for " + pluginFile, ex);
				}
			}
		}
		for (String plugin : wasLoaded) {
			loadPlugin(plugin);
		}
	}

	@Override
	public boolean isPluginAvailable(String name) {
		return availablePlugins.containsKey(name);
	}

	@Override
	public boolean isPluginLoaded(String name) {
		return loadedPlugins.containsKey(name);
	}

	@Override
	public void loadPlugin(String name) {
		if (loadedPlugins.containsKey(name)) {
			Logger.getLogger(ServerImpl.class.getName()).info(String.format("Did not load plugin '%s': Plugin was already loaded.", name));
			return;
		}
		if (availablePlugins.containsKey(name)) {
			try {
				Plugin plugin = Class.forName(availablePlugins.get(name)).asSubclass(Plugin.class).newInstance();
				plugin.startup();
				loadedPlugins.put(name, plugin);
				Logger.getLogger(ServerImpl.class.getName()).info(String.format("Loaded plugin '%s'.", name));
			} catch (Throwable th) {
				Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, String.format("Error while loading plugin '%s':", name), th);
			}
		} else {
			Logger.getLogger(ServerImpl.class.getName()).warning(String.format("Did not load plugin '%s': Plugin could not be found.", name));
		}
	}

	@Override
	public void unloadPlugin(String name) {
		if (!loadedPlugins.containsKey(name)) {
			Logger.getLogger(ServerImpl.class.getName()).info(String.format("Did not unload plugin '%s': Plugin was not loaded.", name));
			return;
		}
		Plugin plugin = loadedPlugins.remove(name);
		try {
			plugin.shutdown();
		} catch (Throwable th) {
			Logger.getLogger(ServerImpl.class.getName()).log(Level.SEVERE, String.format("Error while unloading plugin '%s':", name), th);
		}
		Logger.getLogger(ServerImpl.class.getName()).info(String.format("Unloaded plugin '%s'.", name));
	}

	@Override
	public Plugin getPlugin(String name) {
		return loadedPlugins.get(name);
	}
}
