package com.firstbot.repository;

import com.firstbot.model.Hairdresser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface UserFacebookRepository extends JpaRepository<Hairdresser,Long> {
     List<Hairdresser> findByDayHairCut(String dayHairCut);
     @Query("SELECT f FROM Hairdresser f where TIMESTAMPDIFF(HOUR,CURRENT_TIMESTAMP(),f.dateHairCut) BETWEEN 0 AND 1")
     List<Hairdresser> findByDateHairCut();
     @Modifying
     @Query("update Hairdresser h set h.reminder = ?1 where h.idFacebook = ?2")
     void updateReminder(boolean b, long id);
}
