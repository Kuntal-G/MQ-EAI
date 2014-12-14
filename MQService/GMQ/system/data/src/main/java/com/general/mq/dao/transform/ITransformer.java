package com.general.mq.dao.transform;

import java.util.Collection;

import com.general.mq.common.exception.ApplicationException;

public interface ITransformer<DO, DM> {
	/**
	 * Method to transform domain entities to DTO.
	 * 
	 * @param domain
	 * @param dto
	 * @throws ApplicationException
	 */
	void syncToDto(DM domain, DO dto) throws ApplicationException;

	/**
	 * Method to sync DTO to Domain
	 * 
	 * @param dto
	 * @param domain
	 * @throws ApplicationException
	 */
	void syncToDomain(DO dto, DM domain) throws ApplicationException;

	/**
	 * Method to transform domains entities to DTOs.
	 * 
	 * @param domains
	 * @param dtos
	 * @throws ApplicationException
	 */
	void syncToDto(Collection<DM> domains, Collection<DO> dtos) throws ApplicationException;

	/**
	 * Method to sync DTOs to Domains
	 * 
	 * @param dtos
	 * @param domains
	 * @throws ApplicationException
	 */
	void syncToDomain(Collection<DO> dtos, Collection<DM> domains) throws ApplicationException;

	void nullSafeSyncToDto(DM domain, DO dto) throws ApplicationException;

	void nullSafeSyncToDomain(DO dto, DM domain) throws ApplicationException;

}