package nguyenhuuvu.utils;

import lombok.AllArgsConstructor;
import nguyenhuuvu.entity.JoinEntity;
import nguyenhuuvu.entity.UserEntity;
import nguyenhuuvu.entity.ZoomEntity;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ZoomsUtil {
    public static ZoomEntity createPrivateZoom(UserEntity user) {
        ZoomEntity zoom = new ZoomEntity();
        zoom.setId(user.getUsername());
        zoom.setActive(false);
        zoom.setName(user.getFullname());
        return zoom;
    }
}
