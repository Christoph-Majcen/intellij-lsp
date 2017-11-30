package com.github.gtache.lsp.settings;

import com.github.gtache.lsp.PluginMain;
import com.github.gtache.lsp.client.languageserver.serverdefinition.UserConfigurableServerDefinition;
import com.github.gtache.lsp.client.languageserver.serverdefinition.UserConfigurableServerDefinition$;
import com.github.gtache.lsp.requests.Timeout;
import com.github.gtache.lsp.requests.Timeouts;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

@State(name = "LSPState", storages = @Storage(id = "LSPState", file = "LSPState.xml"))

/**
 * Class representing the state of the LSP settings
 */
public class LSPState implements PersistentStateComponent<LSPState> {

    private static final Logger LOG = Logger.getInstance(LSPState.class);


    public Map<String, String[]> extToServ = new LinkedHashMap<>(10); //Must be public to be saved
    public Map<Timeouts, Integer> timeouts = new EnumMap<>(Timeouts.class);
    public boolean useInplaceRename = false;

    public LSPState() {
    }

    @Nullable
    public static LSPState getInstance() {
        return ServiceManager.getService(LSPState.class);
    }

    public boolean isUseInplaceRename() {
        return useInplaceRename;
    }

    public void setUseInplaceRename(boolean useInplaceRename) {
        this.useInplaceRename = useInplaceRename;
    }

    public Map<String, UserConfigurableServerDefinition> getExtToServ() {
        return UserConfigurableServerDefinition$.MODULE$.fromArrayMap(extToServ);
    }

    public void setExtToServ(final Map<String, UserConfigurableServerDefinition> extToServ) {
        this.extToServ = UserConfigurableServerDefinition$.MODULE$.toArrayMap(extToServ);
    }

    public Map<Timeouts, Integer> getTimeouts() {
        return timeouts;
    }

    public void setTimeouts(final Map<Timeouts, Integer> timeouts) {
        this.timeouts = new EnumMap<>(timeouts);
    }

    @Nullable
    @Override
    public LSPState getState() {
        return this;
    }

    @Override
    public void loadState(final LSPState lspState) {
        XmlSerializerUtil.copyBean(lspState, this);
        LOG.info("LSP State loaded");
        if (extToServ != null && !extToServ.isEmpty()) {
            PluginMain.setExtToServerDefinition(UserConfigurableServerDefinition$.MODULE$.fromArrayMap(extToServ));
        }
        if (timeouts != null && !timeouts.isEmpty()) {
            Timeout.setTimeouts(timeouts);
        }
    }

}
