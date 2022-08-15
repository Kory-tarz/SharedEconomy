package pl.cyryl.finalproject.app.offer;

import org.springframework.stereotype.Service;
import pl.cyryl.finalproject.app.offer.status.Status;
import pl.cyryl.finalproject.app.offer.status.StatusService;
import pl.cyryl.finalproject.app.offer.validation.OfferValidationResult;
import pl.cyryl.finalproject.app.offer.validation.OfferValidator;
import pl.cyryl.finalproject.util.EntityActivationService;

import java.util.List;
import java.util.Optional;

import static pl.cyryl.finalproject.app.offer.validation.OfferValidator.*;

@Service
public class OfferService {

    private final OfferRepository offerRepository;
    private final StatusService statusService;
    private final EntityActivationService entityActivationService;

    public OfferService(OfferRepository offerRepository, StatusService statusService, EntityActivationService entityActivationService) {
        this.offerRepository = offerRepository;
        this.statusService = statusService;
        this.entityActivationService = entityActivationService;
    }

    public boolean isOfferValid(Offer offer) {

        OfferValidator validator = isBetweenDifferentUsers()
                .and(hasItems())
                .and(hasInactiveItems())
                .and(itemsBelongToSubmittingUser())
                .and(itemsBelongToReceivingUser());

        OfferValidationResult result = validator.apply(offer);
        if (result.equals(OfferValidationResult.SUCCESS)) {
            return true;
        } else {
            System.out.println(result);
            // TODO log error
            return false;
        }
    }

    public Offer submitOffer(Offer offer) {
        //TODO check what happens when items were change during operations on offer
        Status status = statusService.getSubmittedStatus();
        Optional<Offer> offerInDb = offerRepository.findById(offer.getId());
        if (offerInDb.isPresent() && !offerInDb.get().getStatus().equals(status)) {
            //We can only change offer if status equals 'submitted'
            //TODO throw exception or sth
        }
        offer.setStatus(status);
        return offerRepository.save(offer);
    }

    public List<Offer> findSubmittedOffersByUser(long id) {
        Status status = statusService.getSubmittedStatus();
        return offerRepository.findAllBySubmittingUserIdAndStatus(id, status);
    }

    public List<Offer> findReceivedOffersByUser(long id) {
        Status status = statusService.getSubmittedStatus();
        return offerRepository.findAllByReceivingUserIdAndStatus(id, status);
    }

    public List<Offer> findAcceptedOffersByUser(long id) {
        Status status = statusService.getAcceptedStatus();
        return offerRepository.findAllAcceptedOffers(id, status);
    }

    public Optional<Offer> findOffer(long id) {
        return offerRepository.findById(id);
    }

    public Optional<Offer> findOfferBelongingToUser(long offerId, long userId) {
        Optional<Offer> offerOpt = offerRepository.findById(offerId);
        if (offerOpt.isPresent()) {
            Offer offer = offerOpt.get();
            if (offer.getSubmittingUser().getId() == userId || offer.getReceivingUser().getId() == userId) {
                return offerOpt;
            }
        }
        return Optional.empty();
    }

    public void acceptOffer(Offer offer) {
        Status status = statusService.getAcceptedStatus();
        offer.setStatus(status);
        offer = offerRepository.save(offer);
        entityActivationService.deactivateOfferItems(offer);
    }

    public void withdrawOffer(long offerId) {
        Offer offerInDb = offerRepository.findById(offerId).orElseThrow();
        Status acceptedStatus = statusService.getAcceptedStatus();
        if (offerInDb.getStatus().equals(acceptedStatus)) {
            Status canceledStatus = statusService.getCanceledStatus();
            offerInDb.setStatus(canceledStatus);
            entityActivationService.activateItemsInOffer(offerInDb);
            offerRepository.save(offerInDb);
        }
    }

}