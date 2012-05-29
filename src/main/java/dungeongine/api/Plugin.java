package dungeongine.api;

/**
 * Base interface for all plugins. The startup and shutdown methods
 * are called when the plugin is enabled or disabled, either by the
 * server owner or by another plugin. The default constructor must
 * be present and accessible.
 *
 * Plugins are to be placed in jars with a configuration file named
 * plugin.yml in the root of the jar in the following format:
 *
 * <pre>
 *     'My Plugin': org.example.myplugin.MyPlugin
 * </pre>
 *
 * In this example, org.example.myplugin.MyPlugin is an implementation
 * of this interface.
 */
public interface Plugin {
	/**
	 * Called when the plugin is enabled.
	 */
	void startup();

	/**
	 * Called when the plugin is disabled.
	 */
	void shutdown();
}
