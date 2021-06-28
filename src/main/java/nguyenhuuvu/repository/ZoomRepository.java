package nguyenhuuvu.repository;

import nguyenhuuvu.entity.ZoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ZoomRepository extends JpaRepository<ZoomEntity, String> {
    ZoomEntity findZoomEntityById(String id);
}
