package org.xdat.data;

/*
 *  Copyright 2019, Enguerrand de Rochefort
 *
 * This file is part of xdat.
 *
 * xdat is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * xdat is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with xdat.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

import java.util.List;

public class ClusterFactory {
    private int nextUid = 0;

    public ClusterFactory() {
    }

    public Cluster newCluster(List<Cluster> currentBuffer) {
        String newClusterName = getUniqueClusterName(currentBuffer);
        return new Cluster(newClusterName, nextUid++);
    }

    public String getUniqueClusterName(List<Cluster> currentBuffer) {
        String name = "Cluster 1";
        int id = 1;
        while (!isNameUnique(name, currentBuffer))
            name = "Cluster " + (id++);
        return name;
    }

    private boolean isNameUnique(String name, List<Cluster> currentBuffer) {
        boolean unique = true;
        for (int i = 0; i < currentBuffer.size(); i++) {
            if (name.equals(currentBuffer.get(i).getName())) {
                unique = false;
                break;
            }
        }
        return unique;
    }
}
