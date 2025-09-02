package com.bitemate.service;

import com.bitemate.entity.AddressBook;
import com.bitemate.result.Result;

import java.util.List;

public interface AddressService {
    /**
     * List out all the current user's address
     * @return
     */
    List<AddressBook> getAddressList();

    /**
     * Add new address
     * @param addressBook
     */
    void addAddress(AddressBook addressBook);

    /**
     * Get address by id
     * @param id
     * @return
     */
    AddressBook getaddressById(Long id);

    /**
     * Update address
     * @param addressBook
     */
    void updateAddress(AddressBook addressBook);

    /**
     * Set default address
     * @param addressBook
     */
    void setDefaultAddress(AddressBook addressBook);

    /**
     * Remove address by id
     * @param id
     */
    void deleteAddressById(Long id);

    /**
     * Get default address
     * @return
     */
    AddressBook getDefaultAddress();
}
