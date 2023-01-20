package com.zouzhao.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.persistence.*;

/**
 * @author 姚超
 * @DATE: 2023-1-17
 * @DESCRIPTION:
 */
@MappedSuperclass
@Data
public class BaseEntity {

        @Id
        @Column(
                length = 36
        )
        @TableId(
                type = IdType.ASSIGN_ID
        )
        // @GeneratedValue(strategy = GenerationType.AUTO)
        private String fdId;
}
