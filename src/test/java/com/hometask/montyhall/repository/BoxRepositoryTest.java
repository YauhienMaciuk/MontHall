package com.hometask.montyhall.repository;

import com.hometask.montyhall.entity.Box;
import com.hometask.montyhall.entity.Game;
import com.hometask.montyhall.entity.GameStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BoxRepositoryTest {

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private GameRepository gameRepository;

    @AfterEach
    void cleanUp() {
        boxRepository.deleteAll();
        gameRepository.deleteAll();
    }

    @Test
    void findByGameIdTest() {
        Game game = new Game();
        game.setStatus(GameStatus.CREATED);

        Box box1 = new Box();
        box1.setGame(game);
        box1.setOpened(true);
        box1.setPicked(false);
        box1.setWinning(false);

        Box box2 = new Box();
        box2.setGame(game);
        box2.setOpened(false);
        box2.setPicked(true);
        box2.setWinning(false);

        Box box3 = new Box();
        box3.setGame(game);
        box3.setOpened(false);
        box3.setPicked(false);
        box3.setWinning(true);

        gameRepository.save(game);
        boxRepository.saveAll(Arrays.asList(box1, box2, box3));
        List<Box> receivedBoxesFromDatabase = boxRepository.findByGameId(game.getId());

        assertEquals(3, receivedBoxesFromDatabase.size());

        assertNotNull(receivedBoxesFromDatabase.get(0).getId());
        assertEquals(receivedBoxesFromDatabase.get(0).getOpened(), box1.getOpened());
        assertEquals(receivedBoxesFromDatabase.get(0).getPicked(), box1.getPicked());
        assertEquals(receivedBoxesFromDatabase.get(0).getWinning(), box1.getWinning());
        assertNotNull(receivedBoxesFromDatabase.get(0).getGame().getId());
        assertEquals(receivedBoxesFromDatabase.get(0).getGame().getStatus(), game.getStatus());

        assertNotNull(receivedBoxesFromDatabase.get(1).getId());
        assertEquals(receivedBoxesFromDatabase.get(1).getOpened(), box2.getOpened());
        assertEquals(receivedBoxesFromDatabase.get(1).getPicked(), box2.getPicked());
        assertEquals(receivedBoxesFromDatabase.get(1).getWinning(), box2.getWinning());
        assertNotNull(receivedBoxesFromDatabase.get(1).getGame().getId());
        assertEquals(receivedBoxesFromDatabase.get(1).getGame().getStatus(), game.getStatus());

        assertNotNull(receivedBoxesFromDatabase.get(2).getId());
        assertEquals(receivedBoxesFromDatabase.get(2).getOpened(), box3.getOpened());
        assertEquals(receivedBoxesFromDatabase.get(2).getPicked(), box3.getPicked());
        assertEquals(receivedBoxesFromDatabase.get(2).getWinning(), box3.getWinning());
        assertNotNull(receivedBoxesFromDatabase.get(2).getGame().getId());
        assertEquals(receivedBoxesFromDatabase.get(2).getGame().getStatus(), game.getStatus());
    }

    @Test
    void tryToFindByGameIdWhenGameDoesNotExistTest() {
        List<Box> receivedBoxesFromDatabase = boxRepository.findByGameId(10L);

        assertEquals(0, receivedBoxesFromDatabase.size());
    }

    @Test
    void findByGameIdAndPicked() {
        Game game = new Game();
        game.setStatus(GameStatus.CREATED);

        Box unpickedBox1 = new Box();
        unpickedBox1.setGame(game);
        unpickedBox1.setOpened(true);
        unpickedBox1.setPicked(false);
        unpickedBox1.setWinning(false);

        Box pickedBox = new Box();
        pickedBox.setGame(game);
        pickedBox.setOpened(false);
        pickedBox.setPicked(true);
        pickedBox.setWinning(false);

        Box unpickedBox2 = new Box();
        unpickedBox2.setGame(game);
        unpickedBox2.setOpened(false);
        unpickedBox2.setPicked(false);
        unpickedBox2.setWinning(true);

        gameRepository.save(game);
        boxRepository.saveAll(Arrays.asList(unpickedBox1, pickedBox, unpickedBox2));
        Box pickedBoxReceivedFromDatabase = boxRepository.findByGameIdAndPicked(game.getId(), true);

        assertNotNull(pickedBoxReceivedFromDatabase.getId());
        assertEquals(pickedBoxReceivedFromDatabase.getOpened(), pickedBox.getOpened());
        assertEquals(pickedBoxReceivedFromDatabase.getPicked(), pickedBox.getPicked());
        assertEquals(pickedBoxReceivedFromDatabase.getWinning(), pickedBox.getWinning());
        assertNotNull(pickedBoxReceivedFromDatabase.getGame().getId());
        assertEquals(pickedBoxReceivedFromDatabase.getGame().getStatus(), game.getStatus());
    }

    @Test
    void tryToFindByGameIdAndPickedWhenPickedBoxDoesNotExist() {
        Game game = new Game();
        game.setStatus(GameStatus.CREATED);

        Box unpickedBox1 = new Box();
        unpickedBox1.setGame(game);
        unpickedBox1.setOpened(true);
        unpickedBox1.setPicked(false);
        unpickedBox1.setWinning(false);

        Box unpickedBox2 = new Box();
        unpickedBox2.setGame(game);
        unpickedBox2.setOpened(false);
        unpickedBox2.setPicked(false);
        unpickedBox2.setWinning(true);

        gameRepository.save(game);
        boxRepository.saveAll(Arrays.asList(unpickedBox1, unpickedBox2));
        Box pickedBoxReceivedFromDatabase = boxRepository.findByGameIdAndPicked(game.getId(), true);

        assertNull(pickedBoxReceivedFromDatabase);
    }

    @Test
    void findByGameIdAndOpenedAndPickedTest() {
        Game game = new Game();
        game.setStatus(GameStatus.CREATED);

        Box unpickedBox1 = new Box();
        unpickedBox1.setGame(game);
        unpickedBox1.setOpened(true);
        unpickedBox1.setPicked(false);
        unpickedBox1.setWinning(false);

        Box pickedBox = new Box();
        pickedBox.setGame(game);
        pickedBox.setOpened(false);
        pickedBox.setPicked(true);
        pickedBox.setWinning(false);

        Box unpickedBox2 = new Box();
        unpickedBox2.setGame(game);
        unpickedBox2.setOpened(false);
        unpickedBox2.setPicked(false);
        unpickedBox2.setWinning(true);

        gameRepository.save(game);
        boxRepository.saveAll(Arrays.asList(unpickedBox1, pickedBox, unpickedBox2));
        Box unpickedAndUnopenedBoxReceivedFromDatabase = boxRepository.findByGameIdAndOpenedAndPicked(game.getId(), false, false);

        assertNotNull(unpickedAndUnopenedBoxReceivedFromDatabase.getId());
        assertEquals(unpickedAndUnopenedBoxReceivedFromDatabase.getOpened(), unpickedBox2.getOpened());
        assertEquals(unpickedAndUnopenedBoxReceivedFromDatabase.getPicked(), unpickedBox2.getPicked());
        assertEquals(unpickedAndUnopenedBoxReceivedFromDatabase.getWinning(), unpickedBox2.getWinning());
        assertNotNull(unpickedAndUnopenedBoxReceivedFromDatabase.getGame().getId());
        assertEquals(unpickedAndUnopenedBoxReceivedFromDatabase.getGame().getStatus(), game.getStatus());
    }

    @Test
    void tryToFindByGameIdAndOpenedAndPickedWhenSuchBoxDoesNotExistTest() {
        Game game = new Game();
        game.setStatus(GameStatus.CREATED);

        Box pickedBox = new Box();
        pickedBox.setGame(game);
        pickedBox.setOpened(false);
        pickedBox.setPicked(true);
        pickedBox.setWinning(false);

        gameRepository.save(game);
        boxRepository.saveAll(Arrays.asList(pickedBox));
        Box unpickedAndUnopenedBoxReceivedFromDatabase = boxRepository.findByGameIdAndOpenedAndPicked(game.getId(), false, false);

        assertNull(unpickedAndUnopenedBoxReceivedFromDatabase);
    }
}
