package com.bitemate.mapper;

import com.bitemate.entity.AddressBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface AddressMapper {
    /**
     * Query the current user's addresses
     * @param addressBook
     * @return
     */
    List<AddressBook> getAddressList(AddressBook addressBook);

    /**
     * Insert address
     * @param addressBook
     */
    @Insert("insert into address_book" +
            "        (user_id, consignee, phone, sex, province_code, province_name, city_code, city_name, district_code," +
            "         district_name, detail, label, is_default)" +
            "        values (#{userId}, #{consignee}, #{phone}, #{sex}, #{provinceCode}, #{provinceName}, #{cityCode}, #{cityName}," +
            "                #{districtCode}, #{districtName}, #{detail}, #{label}, #{isDefault})")
    void insertAddress(AddressBook addressBook);

    /**
     * Get address by id
     * @param id
     * @return
     */
    @Select("select * from address_book where id = #{id}")
    AddressBook getAddressById(Long id);

    /**
     * Update address
     * @param addressBook
     */
    void updateAddress(AddressBook addressBook);

    @Update("update address_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateToNonDefault(AddressBook addressBook);

    /**
     * Remove address by id
     * @param id
     */
    @Delete("delete from address_book where id = #{id}")
    void deleteById(Long id);
}
