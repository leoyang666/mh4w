package com.dwarfeng.jier.mh4w.core.model.cm;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.dwarfeng.jier.mh4w.core.model.obv.ListOperateObverser;

/**
 * 抽象列表模型。
 * <p> 列表模型的抽象实现。
 * <p> 模型中数据的读写均应该是线程安全的。
 * @author DwArFeng
 * @since 0.0.1-beta
 */
public abstract class AbstarctDataListModel<E> implements DataListModel<E> {
	
	/**模型的侦听器集合。*/
	protected final Set<ListOperateObverser<E>> obversers = Collections.newSetFromMap(new WeakHashMap<>());
	/**模型的同步读写锁。*/
	protected final ReadWriteLock lock = new ReentrantReadWriteLock();

	/*
	 * (non-Javadoc)
	 * @see com.dwarfeng.dutil.basic.threads.ExternalReadWriteThreadSafe#getLock()
	 */
	@Override
	public ReadWriteLock getLock() {
		return lock;
	}

	/*
	 * (non-Javadoc)
	 * @see com.dwarfeng.dutil.basic.prog.ObverserSet#getObversers()
	 */
	@Override
	public Set<ListOperateObverser<E>> getObversers() {
		lock.readLock().lock();
		try{
			return Collections.unmodifiableSet(obversers);
		}finally {
			lock.readLock().unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dwarfeng.dutil.basic.prog.ObverserSet#addObverser(com.dwarfeng.dutil.basic.prog.Obverser)
	 */
	@Override
	public boolean addObverser(ListOperateObverser<E> obverser) {
		lock.writeLock().lock();
		try{
			return obversers.add(obverser);
		}finally {
			lock.writeLock().unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dwarfeng.dutil.basic.prog.ObverserSet#removeObverser(com.dwarfeng.dutil.basic.prog.Obverser)
	 */
	@Override
	public boolean removeObverser(ListOperateObverser<E> obverser) {
		lock.writeLock().lock();
		try{
			return obversers.remove(obverser);
		}finally {
			lock.writeLock().unlock();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.dwarfeng.dutil.basic.prog.ObverserSet#clearObverser()
	 */
	@Override
	public void clearObverser() {
		lock.writeLock().lock();
		try{
			obversers.clear();
		}finally {
			lock.writeLock().unlock();
		}
	}


}
