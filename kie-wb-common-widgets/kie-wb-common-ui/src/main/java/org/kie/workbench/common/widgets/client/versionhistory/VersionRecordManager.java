/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.widgets.client.versionhistory;

import org.guvnor.common.services.shared.version.VersionService;
import org.jboss.errai.common.client.api.Caller;
import org.jboss.errai.common.client.api.RemoteCallback;
import org.kie.uberfire.client.common.BusyIndicatorView;
import org.kie.workbench.common.widgets.client.menu.FileMenuBuilder;
import org.kie.workbench.common.widgets.client.resources.i18n.CommonConstants;
import org.kie.workbench.common.widgets.client.versionhistory.event.VersionSelectedEvent;
import org.uberfire.backend.vfs.ObservablePath;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.callbacks.Callback;
import org.uberfire.client.mvp.PlaceManager;
import org.uberfire.commons.validation.PortablePreconditions;
import org.uberfire.java.nio.base.version.VersionRecord;
import org.uberfire.mvp.Command;
import org.uberfire.workbench.model.menu.MenuFactory;
import org.uberfire.workbench.model.menu.MenuItem;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.New;
import javax.inject.Inject;
import java.util.List;

public class VersionRecordManager {

    private VersionMenuDropDownButton button;

    @Inject
    @New
    private FileMenuBuilder menuBuilder;

    private RestorePopup restorePopup;
    private RestoreUtil restoreUtil;
    private Caller<VersionService> versionService;

    @Inject
    private BusyIndicatorView busyIndicatorView;

    @Inject
    private PlaceManager placeManager;

    @Inject
    private Event<VersionSelectedEvent> versionSelectedEvent;

    private Callback<VersionRecord> selectionCallback;
    private List<VersionRecord> versions;
    private ObservablePath pathToLatest;
    private String version;

    @Inject
    public VersionRecordManager(
            VersionMenuDropDownButton button,
            RestorePopup restorePopup,
            RestoreUtil restoreUtil,
            Caller<VersionService> versionService) {
        this.button = button;
        this.restorePopup = restorePopup;
        this.restoreUtil = restoreUtil;
        this.versionService = versionService;
    }

    public void init(
            String version,
            ObservablePath path,
            Callback<VersionRecord> selectionCallback) {

        PortablePreconditions.checkNotNull("path", path);
        this.selectionCallback = PortablePreconditions.checkNotNull("selectionCallback", selectionCallback);

        this.version = version;

        if (version == null) {
            pathToLatest = path;
        }

        loadVersions(path);
    }

    public MenuItem buildMenu() {
        return new VersionMenuItem(button);
    }

    public void setVersions(List<VersionRecord> versions) {
        PortablePreconditions.checkNotNull("versions", versions);

        button.clear();

        if (version == null) {
            version = versions.get(versions.size() - 1).id();
        }

        resolveVersions(versions);

        fillMenu();
    }

    private void fillMenu() {

        button.setTextToLatest();

        fillVersions();

    }

    private void fillVersions() {
        int versionIndex = 1;
        boolean currentHasBeenAdded = false;

        for (final VersionRecord versionRecord : versions) {

            boolean isSelected = isSelected(versionRecord);

            if (isSelected) {
                currentHasBeenAdded = true;
            }

            if (versionIndex < 7 || versions.size() <= 7) {

                addVersionMenuItemLabel(versionIndex, isSelected, versionRecord);
                changeMenuLabelIfNotLatest(versionIndex, versionRecord);

            } else {

                if (!currentHasBeenAdded) {
                    addVersionMenuItemLabel(getCurrentVersionIndex(), true, getCurrentVersionRecord());
                }

                addShowMoreLabel(versionIndex);

                break;

            }

            versionIndex++;
        }
    }

    private int getCurrentVersionIndex() {
        for (int i = 0; i < versions.size(); i++) {
            if (versions.get(i).id().equals(version)) {
                return i + 1;
            }
        }
        return -1;
    }

    private void addVersionMenuItemLabel(int versionIndex, boolean isSelected, VersionRecord versionRecord) {
        button.addLabel(versionRecord,
                versionIndex,
                isSelected,
                getSelectionCommand(versionRecord));
    }

    private void addShowMoreLabel(int versionIndex) {
        button.addViewAllLabel(
                versions.size() - versionIndex,
                new Command() {
                    @Override
                    public void execute() {
                        placeManager.goTo(
                                new VersionHistoryScreenPlace(
                                        getPathToLatest(),
                                        getPathToLatest().getFileName(),
                                        getCurrentVersionRecord().id()));
                    }
                });
    }

    private boolean isSelected(VersionRecord versionRecord) {
        return versionRecord.id().equals(version);
    }

    private void changeMenuLabelIfNotLatest(int versionIndex, VersionRecord versionRecord) {
        if (versionRecord.id().equals(version) && versionIndex != versions.size()) {
            button.setTextToVersion(versionIndex);
        }
    }

    private Command getSelectionCommand(final VersionRecord versionRecord) {
        return new Command() {
            @Override
            public void execute() {
                versionSelectedEvent.fire(new VersionSelectedEvent(
                        getPathToLatest(),
                        versionRecord
                ));
            }
        };
    }

    private void resolveVersions(List<VersionRecord> versions) {
        if (this.versions == null || versions.size() > this.versions.size()) {
            this.versions = versions;
        }
    }

    public MenuFactory.MenuBuilder<MenuFactory.TopLevelMenusBuilder<MenuFactory.MenuBuilder>> newSaveMenuItem() {
        return MenuFactory.newTopLevelMenu(CommonConstants.INSTANCE.Save());
    }

    public boolean isLatest(VersionRecord versionRecord) {
        return versions.get(versions.size() - 1).equals(versionRecord);
    }

    private void setPathToLatest(ObservablePath pathToLatest) {
        this.pathToLatest = PortablePreconditions.checkNotNull("pathToLatest", pathToLatest);
    }

    public ObservablePath getPathToLatest() {
        return pathToLatest;
    }

    /**
     * It is also possible to change the version with an event.
     *
     * @param event
     */
    public void onVersionSelectedEvent(@Observes VersionSelectedEvent event) {
        if (getPathToLatest().equals(event.getPathToFile()) && selectionCallback != null) {
            selectionCallback.callback(event.getVersionRecord());
        }
    }

    public void setVersion(String version) {
        this.version = PortablePreconditions.checkNotNull("version", version);
        if (isCurrentLatest()) {
            button.setTextToLatest();
        } else if (versions != null) {
            button.setTextToVersion(getCurrentVersionIndex());
        }
    }

    public String getVersion() {
        return version;
    }

    public ObservablePath getCurrentPath() {
        if (isCurrentLatest()) {
            return getPathToLatest();
        } else {
            return restoreUtil.createObservablePath(
                    getPathToLatest(),
                    getCurrentVersionRecordUri());
        }
    }

    public boolean isCurrentLatest() {
        return versions == null || getLatestVersionRecord().id().equals(version);
    }

    private VersionRecord getLatestVersionRecord() {
        return versions.get(versions.size() - 1);
    }

    private String getCurrentVersionRecordUri() {
        VersionRecord record = getCurrentVersionRecord();
        if (record == null) {
            return getPathToLatest().toURI();
        } else {
            return record.uri();
        }
    }

    private VersionRecord getCurrentVersionRecord() {
        for (VersionRecord versionRecord : versions) {
            if (versionRecord.id().equals(version)) {
                return versionRecord;
            }
        }
        return null;
    }

    public void restoreToCurrentVersion() {
        restorePopup.show(getCurrentPath(), getCurrentVersionRecordUri());
    }

    private void loadVersions(final ObservablePath path) {
        loadVersions(path, new Callback<List<VersionRecord>>() {
            @Override
            public void callback(List<VersionRecord> records) {
                doesTheVersionExist(records);
            }
        });
    }

    private void doesTheVersionExist(List<VersionRecord> records) {
        boolean found = false;
        for (VersionRecord versionRecord : records) {
            if (versionRecord.id().equals(version)) {
                found = true;
                break;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("Unknown version");
        }
    }

    public void reloadVersions(final Path path) {
        loadVersions(path, new Callback<List<VersionRecord>>() {
            @Override
            public void callback(List<VersionRecord> records) {
                setVersion(records.get(records.size() - 1).id());
            }
        });
    }

    private void loadVersions(final Path path, final Callback<List<VersionRecord>> callback) {
        versionService.call(new RemoteCallback<List<VersionRecord>>() {
            @Override
            public void callback(List<VersionRecord> records) {
                String uri = path.toURI();

                // We should not recreate the path to latest,
                // since the new path instance will not have version support
                if (!path.equals(pathToLatest)) {
                    setPathToLatest(restoreUtil.createObservablePath(path, uri));
                }
                setVersions(records);
                callback.callback(records);
            }
        }).getVersion(path);
    }

    public void clear() {
        selectionCallback = null;
        versions = null;
        pathToLatest = null;
        version = null;
    }

}