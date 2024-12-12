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
import tn.esprit.eventsproject.repositories.EventRepository;
import tn.esprit.eventsproject.repositories.LogisticsRepository;
import tn.esprit.eventsproject.repositories.ParticipantRepository;
import tn.esprit.eventsproject.services.EventServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private LogisticsRepository logisticsRepository;

    @InjectMocks
    private EventServicesImpl eventServices;

    private Event event;
    private Participant participant;
    private Logistics logistics;

    @BeforeEach
    public void setUp() {
        // Initialize test data
        event = new Event();
        event.setDescription("Test Event");

        participant = new Participant();
        participant.setIdPart(1);

        logistics = new Logistics();
        logistics.setReserve(true);
        logistics.setPrixUnit(100);
        logistics.setQuantite(5);
    }

    @Test
    public void testAddParticipant() {
        // Given
        when(participantRepository.save(participant)).thenReturn(participant);

        // When
        Participant result = eventServices.addParticipant(participant);

        // Then
        assertNotNull(result);
        verify(participantRepository, times(1)).save(participant);
    }

    @Test
    public void testAddAffectEvenParticipantById() {
        // Given
        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));
        when(eventRepository.save(event)).thenReturn(event);

        // When
        Event result = eventServices.addAffectEvenParticipant(event, 1);

        // Then
        assertNotNull(result);
        assertEquals(event, result);
        verify(participantRepository, times(1)).findById(1);
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    public void testAddAffectLog() {
        // Given
        when(eventRepository.findByDescription("Test Event")).thenReturn(event);
        when(logisticsRepository.save(logistics)).thenReturn(logistics);

        // When
        Logistics result = eventServices.addAffectLog(logistics, "Test Event");

        // Then
        assertNotNull(result);
        assertEquals(logistics, result);
        verify(eventRepository, times(1)).findByDescription("Test Event");
        verify(logisticsRepository, times(1)).save(logistics);
    }

    @Test
    public void testGetLogisticsDates() {
        // Given
        Set<Logistics> logisticsSet = Set.of(logistics);
        event.setLogistics(logisticsSet);

        when(eventRepository.findByDateDebutBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(event));

        // When
        List<Logistics> result = eventServices.getLogisticsDates(LocalDate.now(), LocalDate.now().plusDays(1));

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(logistics));
        verify(eventRepository, times(1)).findByDateDebutBetween(any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    public void testCalculCout() {
        // Given
        Set<Logistics> logisticsSet = Set.of(logistics);
        event.setLogistics(logisticsSet);

        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", any())).thenReturn(List.of(event));

        // When
        eventServices.calculCout();

        // Then
        assertEquals(500, event.getCout()); // 100 (unit price) * 5 (quantity)
        verify(eventRepository, times(1))
                .findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                        "Tounsi", "Ahmed", any());
        verify(eventRepository, times(1)).save(event);
    }
}
