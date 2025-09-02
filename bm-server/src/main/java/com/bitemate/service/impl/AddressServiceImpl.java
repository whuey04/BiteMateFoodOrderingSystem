package com.bitemate.service.impl;

import com.bitemate.context.BaseContext;
import com.bitemate.entity.AddressBook;
import com.bitemate.mapper.AddressMapper;
import com.bitemate.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;


    /**
     * List out all the current user's address
     *
     * @return
     */
    public List<AddressBook> getAddressList() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        return addressMapper.getAddressList(addressBook);
    }

    /**
     * Add new address
     *
     * @param addressBook
     */
    public void addAddress(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setIsDefault(0);
        addressMapper.insertAddress(addressBook);
    }

    /**
     * Get address by id
     *
     * @param id
     * @return
     */
    public AddressBook getaddressById(Long id) {
        return addressMapper.getAddressById(id);
    }

    /**
     * Update address
     *
     * @param addressBook
     */
    public void updateAddress(AddressBook addressBook) {
        addressMapper.updateAddress(addressBook);
    }

    /**
     * Set default address
     *
     * @param addressBook
     */
    @Transactional
    public void setDefaultAddress(AddressBook addressBook) {
        // Set all current user's address to non default
        addressBook.setIsDefault(0);
        addressBook.setUserId(BaseContext.getCurrentId());
        addressMapper.updateToNonDefault(addressBook);

        // Set current address as default
        addressBook.setIsDefault(1);
        addressMapper.updateAddress(addressBook);
    }

    /**
     * Remove address by id
     *
     * @param id
     */
    public void deleteAddressById(Long id) {
        addressMapper.deleteById(id);
    }

    /**
     * Get default address
     *
     * @return
     */
    public AddressBook getDefaultAddress() {
        AddressBook addressBook = AddressBook.builder()
                .isDefault(1)
                .userId(BaseContext.getCurrentId())
                .build();
        List<AddressBook> addressList = addressMapper.getAddressList(addressBook);

        if (addressList != null && addressList.size() == 1) {
            return addressList.get(0);
        }

        return null;
    }
}
