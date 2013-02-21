package kree;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Kree extends AbstractUIPlugin {
    private static final String ENABLED = "ENABLED";
    public static final String PLUGIN_ID = "pm.eclipse.kree";
    private static Kree plugin;

    public Kree() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    public static Kree getDefault() {
        return plugin;
    }

    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin("pm.eclipse.kree", path);
    }

    public boolean isEnabled() {
        if (getPreferenceStore().contains("ENABLED"))
            return getPreferenceStore().getBoolean("ENABLED");
        else
            return false;
    }

    public void setEnabled(boolean flag) {
        getPreferenceStore().setValue("ENABLED", flag);
    }

    public static void logError(Object source, String msg, Throwable error) {
        String src = "";
        if (source instanceof Class)
            src = ((Class) source).getName();
        else if (source != null)
            src = source.getClass().getName();
        getDefault().getLog().log(new Status(4, "pm.eclipse.kree", 1, (new StringBuilder("[")).append(src).append("] ").append(msg).toString(), error));
    }
}
