package tn.esprit.eventsproject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.services.IEventServices;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventRestControllerTest {

    @Mock
    private IEventServices eventServices;

    @InjectMocks
    private EventRestControllerTest eventRestController;

    private Event event;
    private Participant participant;
    private Logistics logistics;

    @BeforeEach
    public void setUp() {
        // Initialisation des objets mockés
        event = new Event();
        event.setDescription("Test Event");

        participant = new Participant();
        participant.setIdPart(1);

        logistics = new Logistics();
        logistics.setReserve(true);
        logistics.setPrixUnit(100);
        logistics.setQuantite(5);
    }

//    @Test
//    public void testAddParticipant() {
//        // Given
//        when(eventServices.addParticipant(participant)).thenReturn(participant);
//
//        // When
//        Participant result = eventRestController.addParticipant(participant);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(participant, result);
//        verify(eventServices, times(1)).addParticipant(participant);
//    }
//
//    @Test
//    public void testAddEventPart() {
//        // Given
//        when(eventServices.addAffectEvenParticipant(event, 1)).thenReturn(event);
//
//        // When
//        Event result = eventRestController.addEventPart(event, 1);
//
//        // Then
//        assertNotNull(result);
//        assertEquals(event, result);
//        verify(eventServices, times(1)).addAffectEvenParticipant(event, 1);
//    }
//
//    @Test
//    public void testAddAffectLog() {
//        // Given
//        when(eventServices.addAffectLog(logistics, "Test Event")).thenReturn(logistics);
//
//        // When
//        Logistics result = eventRestController.addAffectLog(logistics, "Test Event");
//
//        // Then
//        assertNotNull(result);
//        assertEquals(logistics, result);
//        verify(eventServices, times(1)).addAffectLog(logistics, "Test Event");
//    }
//
//    @Test
//    public void testGetLogistiquesDates() {
//        // Given
//        when(eventServices.getLogisticsDates(any(LocalDate.class), any(LocalDate.class)))
//                .thenReturn(List.of(logistics));
//
//        // When
//        List<Logistics> result = eventRestController.getLogistiquesDates(LocalDate.now(), LocalDate.now().plusDays(1));
//
//        // Then
//        assertNotNull(result);
//        assertEquals(1, result.size());
//        assertTrue(result.contains(logistics));
//        verify(eventServices, times(1)).getLogisticsDates(any(LocalDate.class), any(LocalDate.class));
//    }
}
