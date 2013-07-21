package com.renren.api.client.utils;

import java.util.zip.CRC32;

/**
 * @author 李勇 2011-2-21
 */
public class EmailHashUtil {

    public static String getEmailHash(String email) {
        CRC32 crc = new CRC32();
        crc.reset();
        crc.update(email.getBytes());
        Long emailCRC = crc.getValue();
        String emailMD5 = Md5Utils.md5(email);
        return emailCRC.toString() + '_' + emailMD5;
    }
}
