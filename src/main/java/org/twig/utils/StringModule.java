package org.twig.utils;

/*
 * **THIS FILE IS JUST A PART OF THE ORIGINAL FILE. ORIGINAL FOUND AT https://github.com/CleverCloud/Quercus/blob/d60c80f3d24cdf6fef26a2c3065d1fae093133f4/quercus/src/main/java/com/caucho/quercus/lib/string/StringModule.java **
 *
 * Copyright (c) 1998-2010 Caucho Technology -- all rights reserved
 *
 * This file is part of Resin(R) Open Source
 *
 * Each copy or derived work must preserve the copyright notice and this
 * notice unmodified.
 *
 * Resin Open Source is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Resin Open Source is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, or any warranty
 * of NON-INFRINGEMENT.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Resin Open Source; if not, write to the
 *
 *   Free Software Foundation, Inc.
 *   59 Temple Place, Suite 330
 *   Boston, MA 02111-1307  USA
 *
 * @author Scott Ferguson
 */
public class StringModule {
    /**
     * Strip out the backslashes, recognizing the escape sequences, octal,
     * and hexadecimal representations.
     *
     * @param source the string to clean
     */
    public static String stripcslashes(String source) {
        if (source == null) {
            source = "";
        }

        StringBuilder result = new StringBuilder(source.length());

        int length = source.length();

        for (int i = 0; i < length; i++) {
            int ch = source.charAt(i);

            if (ch == '\\') {
                i++;

                if (i == length) {
                    ch = '\\';
                } else {
                    ch = source.charAt(i);

                    switch (ch) {
                        case 'a':
                            ch = 0x07;
                            break;
                        case 'b':
                            ch = '\b';
                            break;
                        case 't':
                            ch = '\t';
                            break;
                        case 'n':
                            ch = '\n';
                            break;
                        case 'v':
                            ch = 0xb;
                            break;
                        case 'f':
                            ch = '\f';
                            break;
                        case 'r':
                            ch = '\r';
                            break;
                        case 'x':
                            // up to two digits for a hex number
                            if (i + 1 == length) {
                                break;
                            }

                            int digitValue = hexToDigit(source.charAt(i + 1));

                            if (digitValue < 0) {
                                break;
                            }

                            ch = digitValue;
                            i++;

                            if (i + 1 == length) {
                                break;
                            }

                            digitValue = hexToDigit(source.charAt(i + 1));

                            if (digitValue < 0) {
                                break;
                            }

                            ch = ((ch << 4) | digitValue);
                            i++;

                            break;
                        default:
                            // up to three digits from 0 to 7 for an octal number
                            digitValue = octToDigit((char) ch);

                            if (digitValue < 0) {
                                break;
                            }

                            ch = digitValue;

                            if (i + 1 == length) {
                                break;
                            }

                            digitValue = octToDigit(source.charAt(i + 1));

                            if (digitValue < 0) {
                                break;
                            }

                            ch = ((ch << 3) | digitValue);
                            i++;

                            if (i + 1 == length) {
                                break;
                            }

                            digitValue = octToDigit(source.charAt(i + 1));

                            if (digitValue < 0) {
                                break;
                            }

                            ch = ((ch << 3) | digitValue);
                            i++;
                    }
                }
            } // if ch == '/'

            result.append((char) ch);
        }

        return result.toString();
    }

    protected static int hexToDigit(char ch) {
        if ('0' <= ch && ch <= '9') {
            return ch - '0';
        } else if ('a' <= ch && ch <= 'f') {
            return ch - 'a' + 10;
        } else if ('A' <= ch && ch <= 'F') {
            return ch - 'A' + 10;
        } else {
            return -1;
        }
    }

    protected static int octToDigit(char ch) {
        if ('0' <= ch && ch <= '7') {
            return ch - '0';
        } else {
            return -1;
        }
    }
}
