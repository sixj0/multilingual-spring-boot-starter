/**
 * runlion.com Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.sixj.multilingual.utils;

import org.springframework.util.StringUtils;

/**
 * @author sixiaojie
 * @date 2019-12-19 9:50
 */
public class MultilingualJsonUtil {
    /**
     * 格式化json字符串
     * @param jsonStr
     * @return
     */
    public static String formatJson(String jsonStr) {
        if (StringUtils.isEmpty(jsonStr)){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }
    /**
     * 添加space
     * @param sb
     * @param indent
     */
    public static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
}
