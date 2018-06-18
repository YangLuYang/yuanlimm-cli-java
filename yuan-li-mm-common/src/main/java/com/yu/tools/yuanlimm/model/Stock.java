package com.yu.tools.yuanlimm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 股票
 */
@NoArgsConstructor
@Data
public class Stock implements Serializable {
    private String avatar;
    private String code;
    private String music_link;
    private String name;
    private Double price;
    private Double price_day_open;
    private Long total_share;
    private String video_link;
}
