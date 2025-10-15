package example.cashcard;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    private final CashCardRepository cashCardRepository;

    private CashCardController(final CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }
    
    @GetMapping("/{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable final Long requestedId, final Principal principal) {
        final CashCard cashCard = findCashCard(requestedId, principal);
        if (cashCard != null) {
            return ResponseEntity.ok(cashCard);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    private ResponseEntity<Void> createCashCard(@RequestBody final CashCard newCashCardRequest, final UriComponentsBuilder uriComponentsBuilder, Principal principal) {
        final CashCard cashCardWithOwner = new CashCard(null, newCashCardRequest.amount(), principal.getName());
        final CashCard saveCashCard = cashCardRepository.save(cashCardWithOwner);
        final URI locationOfNewCashCard = uriComponentsBuilder.path("cashcards/{id}").buildAndExpand(saveCashCard.id()).toUri();
        return ResponseEntity.created(locationOfNewCashCard).build();
    }

    @GetMapping
    private ResponseEntity<List<CashCard>> findAll(final Pageable pageable, final Principal principal) {
        final Page<CashCard> page = cashCardRepository.findByOwner(principal.getName(), PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSortOr(Sort.by(Sort.Direction.ASC, "amount"))));
        return ResponseEntity.ok(page.getContent());
    }

    @PutMapping("/{requestedId}")
    private ResponseEntity<Void> putCashCard(@PathVariable final Long requestedId, @RequestBody final CashCard cashCardUpdate, final Principal principal) {
        final CashCard cashCard = findCashCard(requestedId, principal);
        if (cashCard != null) {
            final CashCard updatedCashCard = new CashCard(cashCard.id(), cashCardUpdate.amount(), principal.getName());
            cashCardRepository.save(updatedCashCard);
            return ResponseEntity.noContent().build();   
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteCashCard(@PathVariable final Long id, final Principal principal) {
        if (cashCardRepository.existsByIdAndOwner(id, principal.getName())) {
            cashCardRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private CashCard findCashCard(final Long requestedId, final Principal principal) {
        final CashCard cashCard = cashCardRepository.findByIdAndOwner(requestedId, principal.getName());
        return cashCard;
    }
}
