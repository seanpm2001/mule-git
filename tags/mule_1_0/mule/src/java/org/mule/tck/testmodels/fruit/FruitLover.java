/* 
 * $Header$
 * $Revision$
 * $Date$
 * ------------------------------------------------------------------------------------------------------
 * 
 * Copyright (c) SymphonySoft Limited. All rights reserved.
 * http://www.symphonysoft.com
 * 
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file. 
 *
 */

package org.mule.tck.testmodels.fruit;

import java.util.ArrayList;
import java.util.List;


public class FruitLover
{
    private List eatList = new ArrayList();
    private String catchphrase;

    public FruitLover(String catchphrase)
    {
        this.catchphrase = catchphrase;
    }

    public void eatFruit(Fruit fruit)
    {
        fruit.bite();
        eatList.add(fruit.getClass());
    }

    public List getEatList()
    {
        return eatList;
    }

    public String speak()
    {
        return catchphrase;
    }
}