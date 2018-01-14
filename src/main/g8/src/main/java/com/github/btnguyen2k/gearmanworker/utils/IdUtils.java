package com.github.btnguyen2k.gearmanworker.utils;

import com.github.ddth.commons.utils.IdGenerator;

/**
 * ID helper class
 * 
 * @author Thanh Nguyen <btnguyen2k@gmail.com>
 * @since template-0.1.0
 */
public class IdUtils {
    private final static IdGenerator IDGEN = IdGenerator.getInstance(IdGenerator.getMacAddr());

    public static String nextId() {
        return IDGEN.generateId128Ascii().toLowerCase();
    }
}
