package com.sixj.multilingual.translater.baidu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author sixiaojie
 * @date 2020-08-17-11:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransResponse implements Serializable {
    private String from;
    private String to;
    private List<TransResult> trans_result;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class TransResult implements Serializable{
    private String src;
    private String dst;
}