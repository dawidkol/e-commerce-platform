package pl.dk.ecommerceplatform.address;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.ecommerceplatform.address.dtos.AddressDto;
import pl.dk.ecommerceplatform.address.dtos.SaveAddressDto;
import pl.dk.ecommerceplatform.error.exceptions.address.AddressNotFoundException;
import pl.dk.ecommerceplatform.error.exceptions.address.UpdateAddressException;
import pl.dk.ecommerceplatform.error.exceptions.server.ServerException;

@Service
@AllArgsConstructor
class AddressService {

    private final AddressRepository addressRepository;
    private final AddressDtoMapper addressDtoMapper;

    @Transactional
    public AddressDto createShippingAddress(SaveAddressDto saveAddressDto) {
        Address addressToSave = addressDtoMapper.map(saveAddressDto);
        Address savedAddress = addressRepository.save(addressToSave);
        return addressDtoMapper.map(savedAddress);
    }

    @Transactional
    public void updateShippingAddress(Long id, SaveAddressDto saveAddressDto) {
        addressRepository.findById(id).orElseThrow(AddressNotFoundException::new);
        Address newAddress = addressDtoMapper.map(saveAddressDto);
        addressRepository.save(newAddress);
    }
}
