/*
 *  Copyright (C) 2012 John Casey.
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.apache.maven.io.util;

/**
 * Class Counter.
 * 
 * @version $Revision$ $Date$
 */
public class IndentationCounter
{

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /**
     * Field currentIndex.
     */
    private int currentIndex = 0;

    /**
     * Field level.
     */
    private final int level;

    // ----------------/
    // - Constructors -/
    // ----------------/

    public IndentationCounter( final int depthLevel )
    {
        level = depthLevel;
    } // -- org.apache.maven.model.io.jdom.Counter(int)

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method getCurrentIndex.
     * 
     * @return int
     */
    public int getCurrentIndex()
    {
        return currentIndex;
    } // -- int getCurrentIndex()

    /**
     * Method getDepth.
     * 
     * @return int
     */
    public int getDepth()
    {
        return level;
    } // -- int getDepth()

    /**
     * Method increaseCount.
     */
    public void increaseCount()
    {
        currentIndex = currentIndex + 1;
    } // -- void increaseCount()

}