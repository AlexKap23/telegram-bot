package com.alexandros.teleram.bot.services;

import com.alexandros.teleram.bot.model.Idea;
import com.alexandros.teleram.bot.repositories.IdeaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class IdeaServiceImpl implements IdeaService {
	private final IdeaRepository ideaRepository;

	public IdeaServiceImpl(IdeaRepository ideaRepository) {
		this.ideaRepository = ideaRepository;
	}

	@Override
	public List<Idea> findAll() {
		List<Idea> ideas = new ArrayList<>();
		ideaRepository.findAll().iterator().forEachRemaining(ideas::add);
		return ideas;
	}

	@Override
	public Idea findById(Long id) {
		Optional<Idea> optionalIdea = ideaRepository.findById(id);
		if(!optionalIdea.isPresent()){
			throw new RuntimeException("Idea with this id does not exist "+ id);
		}
		return optionalIdea.get();
	}

	@Override
	public Idea save(Idea object) {
		if(object !=null){
			ideaRepository.save(object);
			return object;
		}else{
			throw new RuntimeException("Cannot save a null object");
		}
	}

	@Override
	public void delete(Idea object) {
		if(object != null){
			ideaRepository.delete(object);
		}else{
			throw new RuntimeException("Cannot delete an null object");
		}
	}

	@Override
	public void deleteById(Long id) {
		Optional<Idea> optionalIdea = ideaRepository.findById(id);
		if(optionalIdea.isPresent()){
			ideaRepository.delete(optionalIdea.get());
		}else{
			throw new RuntimeException("Cannot delete idea with id: "+id);
		}
	}
}
