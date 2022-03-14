package com.song.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Song
 * @date 2022/3/10 14:05
 * @Version 1.0
 */
@Data
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Price implements Serializable {
    private String name;
    private String worth;
    private String type;
}
