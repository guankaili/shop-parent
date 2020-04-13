package com.world.model.qly;

import com.world.data.mysql.Bean;
import lombok.Data;

import java.io.Serializable;

@Data
public class CbmMagUserExt extends Bean implements Serializable {
    private static final long serialVersionUID = -44387738498311118L;

    private String user_id;
    private String nickname;
}
