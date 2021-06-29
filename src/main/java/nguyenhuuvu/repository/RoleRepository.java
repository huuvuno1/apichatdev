package nguyenhuuvu.repository;

import nguyenhuuvu.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
    RoleEntity findRoleEntityByName(String name);
}
