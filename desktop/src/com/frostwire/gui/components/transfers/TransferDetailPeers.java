/*
 * Created by Angel Leon (@gubatron), Alden Torres (aldenml),
 * Marcelina Knitter (@marcelinkaaa), Jose Molina (@votaguz)
 * Copyright (c) 2011-2018, FrostWire(R). All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.frostwire.gui.components.transfers;

import com.frostwire.gui.bittorrent.BittorrentDownload;
import com.frostwire.jlibtorrent.PeerInfo;
import com.frostwire.util.Logger;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;

public final class TransferDetailPeers extends JPanel implements TransferDetailComponent.TransferDetailPanel {
    private static final Logger LOG = Logger.getLogger(TransferDetailPeers.class);

    private final TransferDetailPeersTableMediator tableMediator;
    private BittorrentDownload btDownload;

    public TransferDetailPeers() {
        tableMediator = new TransferDetailPeersTableMediator();
        setLayout(new MigLayout("fill"));
        add(tableMediator.getComponent(), "growx, growy");
    }

    @Override
    public void updateData(BittorrentDownload btDownload) {
        if (btDownload != null && btDownload.getDl() != null) {
            try {
                ArrayList<PeerInfo> items = btDownload.getDl().getTorrentHandle().peerInfo();
                if (items != null && items.size() > 0) {
                    if (this.btDownload != btDownload) {
                        this.btDownload = btDownload;
                        tableMediator.clearTable();
                        int i = 0;
                        for (PeerInfo item : items) {
                            tableMediator.add(new PeerItemHolder(i++, item));
                        }
                    } else {
                        int i = 0;
                        for (PeerInfo item : items) {
                            tableMediator.update(new PeerItemHolder(i++, item));
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    public class PeerItemHolder {
        public final int peerOffset;
        final PeerInfo peerItem;

        PeerItemHolder(int peerOffset, PeerInfo peerItem) {
            this.peerOffset = peerOffset;
            this.peerItem = peerItem;
        }

        @Override
        public int hashCode() {
            return peerOffset;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof PeerItemHolder && ((PeerItemHolder) obj).peerOffset == peerOffset;
        }
    }
}