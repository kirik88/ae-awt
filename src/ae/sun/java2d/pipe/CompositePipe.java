/*
 * Copyright 1997-2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

package ae.sun.java2d.pipe;

import ae.java.awt.Rectangle;
import ae.java.awt.Shape;
import ae.sun.java2d.SunGraphics2D;

/**
 * This interface defines the set of calls used by a rendering pipeline
 * based on an AATileGenerator to communicate the alpha tile sequence
 * to the output (compositing) stages of the pipeline.
 */
public interface CompositePipe {
    public Object startSequence(SunGraphics2D sg, Shape s, Rectangle dev,
                                int[] abox);

    public boolean needTile(Object context, int x, int y, int w, int h);

    public void renderPathTile(Object context,
                               byte[] atile, int offset, int tilesize,
                               int x, int y, int w, int h);

    public void skipTile(Object context, int x, int y);

    public void endSequence(Object context);
}
