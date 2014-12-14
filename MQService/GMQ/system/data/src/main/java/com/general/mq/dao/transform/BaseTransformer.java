package com.general.mq.dao.transform;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.general.mq.common.error.ApplicationCode;
import com.general.mq.common.exception.ApplicationException;
import com.general.mq.common.util.CollectionUtils;
import com.general.mq.common.util.Matcher;

public abstract class BaseTransformer<DTO, DOMAIN> implements ITransformer<DTO, DOMAIN> {
	/**
	 * {@link ITransformer} <br/>
	 * Final implementation of list to list transformer. This will merged
	 * matching objects of both side, add new objects from new to right and
	 * remove extra objects from right
	 */
	public final void syncToDomain(Collection<DTO> dtos, Collection<DOMAIN> domains) throws ApplicationException {
		final Set<DOMAIN> inserts = new HashSet<DOMAIN>();
		final Set<DOMAIN> deletes = new HashSet<DOMAIN>();

		if (CollectionUtils.isNotEmpty(dtos) && CollectionUtils.isNotEmpty(domains)) {
			deletes.addAll(domains);
			for (final DTO dto : dtos) {
				if (isInvalidRequestObject(dto)) {
					continue;
				}
				final DOMAIN find = CollectionUtils.find(domains, new Matcher<DOMAIN>() {
					@Override
					public boolean match(DOMAIN domain) {
						return similar(dto, domain);
					}
				});
				if (find != null) {
					deletes.remove(find);
					syncToDomain(dto, find);
				} else {
					final DOMAIN domain = getDomainInstance();
					syncToDomain(dto, domain);
					preDomainCreation(domain);
					inserts.add(domain);
				}

			}
		} else if (CollectionUtils.isEmpty(dtos) && CollectionUtils.isNotEmpty(domains)) {
			for (DOMAIN domain : domains) {
				preDomainDeletion(domain);
			}
			deletes.addAll(domains);
		} else if (CollectionUtils.isNotEmpty(dtos) && CollectionUtils.isEmpty(domains)) {
			for (DTO dto : dtos) {
				if (isInvalidRequestObject(dto)) {
					continue;
				}
				final DOMAIN domain = getDomainInstance();
				syncToDomain(dto, domain);
				preDomainCreation(domain);
				inserts.add(domain);
			}
		}
		for (DOMAIN domain : deletes) {
			preDomainDeletion(domain);
		}
		if(domains != null){
			domains.removeAll(deletes);
			domains.addAll(inserts);
		}
	}

	/**
	 * {@link ITransformer} <br/>
	 * Final implementation of list to list transformer. This will merged
	 * matching objects of both side, add new objects from new to right and
	 * remove extra objects from right
	 */
	public final void syncToDto(Collection<DOMAIN> domains, Collection<DTO> dtos) throws ApplicationException {
		final Set<DTO> inserts = new HashSet<DTO>();
		final Set<DTO> deletes = new HashSet<DTO>();

		if (CollectionUtils.isNotEmpty(dtos) && CollectionUtils.isNotEmpty(domains)) {
			deletes.addAll(dtos);
			for (final DOMAIN domain : domains) {
				final DTO find = CollectionUtils.find(dtos, new Matcher<DTO>() {
					@Override
					public boolean match(DTO dto) {
						return similar(dto, domain);
					}
				});
				if (find != null) {
					deletes.remove(find);
					syncToDto(domain, find);
				} else {
					final DTO dto = getDtoInstance();
					syncToDto(domain, dto);
					inserts.add(dto);
				}
			}
		} else if (CollectionUtils.isEmpty(dtos) && CollectionUtils.isNotEmpty(domains)) {
			for (DOMAIN domain : domains) {
				final DTO dto = getDtoInstance();
				syncToDto(domain, dto);
				inserts.add(dto);
			}
		}
		dtos.removeAll(deletes);
		dtos.addAll(inserts);
	}

	/**
	 * This will take care of creating a new DTO object from the generic type
	 * passed at the class level
	 * 
	 * @return
	 * @throws SystemException
	 * @throws ApplicationException
	 */
	@SuppressWarnings("unchecked")
	private DTO getDtoInstance() throws ApplicationException {
		final ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		final Class<DTO> clazz = (Class<DTO>) type.getActualTypeArguments()[0];
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new ApplicationException(e,ApplicationCode.TRANSFORM_FAILURE);
		}
	}

	/**
	 * This will take care of creating a new DTO object from the generic type
	 * passed at the class level
	 * 
	 * @return
	 * @throws SystemException
	 * @throws ApplicationException
	 */
	@SuppressWarnings("unchecked")
	private DOMAIN getDomainInstance() throws ApplicationException{
		final ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		final Class<DOMAIN> clazz = (Class<DOMAIN>) type.getActualTypeArguments()[1];
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new ApplicationException(e,ApplicationCode.TRANSFORM_FAILURE);
		}
	}

	/**
	 * This method must be implemented to compare a DTO and domain object.
	 * 
	 * @param dto
	 *            DTO
	 * @param domain
	 *            Domain
	 * @return
	 */
	protected abstract boolean similar(DTO dto, DOMAIN domain);

	/**
	 * This method is a callback method for all NEW domain entities, whenever a
	 * new domain entity will be created in any of the transform methods, this
	 * method gets called. Use this method to set necessary references in the
	 * domain entity
	 * 
	 * @param domain
	 */
	protected abstract void preDomainCreation(DOMAIN domain);

	protected abstract void preDomainDeletion(DOMAIN domain);

	protected boolean isInvalidRequestObject(DTO dto) {
		return false;
	}

	@Override
	public final void nullSafeSyncToDomain(DTO dto, DOMAIN domain) throws ApplicationException {
		if (dto != null && domain != null) {
			syncToDomain(dto, domain);
		}
	}

	@Override
	public final void nullSafeSyncToDto(DOMAIN domain, DTO dto) throws ApplicationException {
		if (dto != null && domain != null) {
			syncToDto(domain, dto);
		}

	}


}