/*
 * Copyright 2000-2004 Sun Microsystems, Inc.  All Rights Reserved.
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

package ae.com.sun.imageio.plugins.jpeg;

import ae.javax.imageio.spi.ImageWriterSpi;
import ae.javax.imageio.spi.ServiceRegistry;
import ae.javax.imageio.spi.IIORegistry;
import ae.javax.imageio.ImageWriter;
import ae.javax.imageio.ImageTypeSpecifier;
import ae.javax.imageio.IIOException;

import ae.java.awt.image.ColorModel;
import ae.java.awt.image.IndexColorModel;
import ae.java.awt.image.SampleModel;
import java.util.Locale;

public class JPEGImageWriterSpi extends ImageWriterSpi {

    private static String [] readerSpiNames =
        {"com.sun.imageio.plugins.jpeg.JPEGImageReaderSpi"};

    private boolean registered = false;

    public JPEGImageWriterSpi() {
        super(JPEG.vendor,
              JPEG.version,
              JPEG.names,
              JPEG.suffixes,
              JPEG.MIMETypes,
              "com.sun.imageio.plugins.jpeg.JPEGImageWriter",
              STANDARD_OUTPUT_TYPE,
              readerSpiNames,
              true,
              JPEG.nativeStreamMetadataFormatName,
              JPEG.nativeStreamMetadataFormatClassName,
              null, null,
              true,
              JPEG.nativeImageMetadataFormatName,
              JPEG.nativeImageMetadataFormatClassName,
              null, null
              );
    }

    public String getDescription(Locale locale) {
        return "Standard JPEG Image Writer";
    }

    public void onRegistration(ServiceRegistry registry,
                               Class<?> category) {
        if (registered) {
            return;
        }
        try {
            java.security.AccessController.doPrivileged(
                new ae.sun.security.action.LoadLibraryAction("jpeg"));
        } catch (Throwable e) { // Fail on any Throwable
            // if it can't be loaded, deregister and return
            registry.deregisterServiceProvider(this);
            return;
        }

        registered = true;
    }

    public boolean isFormatLossless() {
        return false;
    }

    public boolean canEncodeImage(ImageTypeSpecifier type) {
        SampleModel sampleModel = type.getSampleModel();

        // Find the maximum bit depth across all channels
        int[] sampleSize = sampleModel.getSampleSize();
        int bitDepth = sampleSize[0];
        for (int i = 1; i < sampleSize.length; i++) {
            if (sampleSize[i] > bitDepth) {
                bitDepth = sampleSize[i];
            }
        }

        // 4450894: Ensure bitDepth is between 1 and 8
        if (bitDepth < 1 || bitDepth > 8) {
            return false;
        }

        return true;
    }

    public ImageWriter createWriterInstance(Object extension)
        throws IIOException {
        return new JPEGImageWriter(this);
    }
}
