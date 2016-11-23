package com.firstbot.repository;

import com.firstbot.constant.Day;
import com.firstbot.entity.Hairdresser;
import com.firstbot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Transactional
public interface HairdresserRepository extends JpaRepository<Hairdresser, Long> {
    List<Hairdresser> findAll();

    List<Hairdresser> findByUser(User user);

    List<Hairdresser> findByDayHairCut(Day day);

    //List<Hairdresser> findFutureHairCut();
    @Query("SELECT f FROM Hairdresser f WHERE TIMESTAMPDIFF(MINUTE,CURRENT_TIMESTAMP(),f.dateHairCut) between 0 and 60")
    List<Hairdresser> findReminderByDateHairCut();

    @Modifying
    @Query("UPDATE Hairdresser h SET h.reminder = ?1 WHERE h.user = ?2 AND h.dateHairCut = ?3")
    void updateReminder(boolean reminder, User user, LocalDateTime dateHairCut);
}
