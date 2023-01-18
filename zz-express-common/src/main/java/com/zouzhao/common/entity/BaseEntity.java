package com.zouzhao.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
@MappedSuperclass
public class BaseEntity {

        @Id
        @Column(
                length = 36
        )
        @TableId(
                type = IdType.ASSIGN_ID
        )
        private String fdId;
}
