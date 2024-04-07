package pl.dk.ecommerceplatform.address;

import org.springframework.stereotype.Service;
import pl.dk.ecommerceplatform.address.Address;
import pl.dk.ecommerceplatform.address.dtos.AddressDto;
import pl.dk.ecommerceplatform.address.dtos.SaveAddressDto;

@Service
class AddressDtoMapper {

    public Address map(SaveAddressDto saveAddressDto) {
        return Address.builder()
                .id(saveAddressDto.id())
                .postalCode(saveAddressDto.postalCode())
                .street(saveAddressDto.street())
                .buildingNumber(saveAddressDto.buildingNumber())
                .apartmentNumber(saveAddressDto.apartmentNumber())
                .phoneNumber(saveAddressDto.phoneNumber())
                .build();
    }

    public AddressDto map(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .postalCode(address.getPostalCode())
                .street(address.getStreet())
                .buildingNumber(address.getBuildingNumber())
                .apartmentNumber(address.getApartmentNumber())
                .phoneNumber(address.getPhoneNumber())
                .build();
    }

}
