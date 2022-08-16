package com.trabalho.br.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.trabalho.br.model.Prato;
import com.trabalho.br.repository.PratoRepository;
import com.trabalho.br.util.ImageUtil;

@Service
public class PratoService {

    @Autowired
    private PratoRepository pratoRepository;

    @Autowired
    private PratoService pratoService;

    public void save(Prato prato, @RequestParam(value = "imagem") MultipartFile imagem) {
        prato.setStatus(1);
        prato.setimgPrato("imgPrato");
        pratoRepository.save(prato);
        String caminho = "images/" + prato.getNome() + prato.getIdPrato() + ".png";
        prato.setimgPrato(caminho);
        pratoRepository.save(prato);
        ImageUtil.saveImage(caminho, imagem);
    }

    public void update(Prato prato, @RequestParam(value = "imagem") MultipartFile imagem) {
        Prato oldPrato = pratoService.serchById((long) prato.getIdPrato());
        String caminho = oldPrato.getimgPrato();
        ImageUtil.deleteImage(caminho);
        prato.setStatus(1);
        pratoRepository.save(prato);
        caminho = "images/" + prato.getNome() + prato.getIdPrato() + ".png";
        prato.setimgPrato(caminho);
        pratoRepository.save(prato);
        ImageUtil.saveImage(caminho, imagem);
    }

    public List<Prato> listPratos() {
        return pratoRepository.findAll();
    }

    public void delete(Long id) {
        String caminho = "images/" + serchById(id).getNome() + serchById(id).getIdPrato() + ".png";
        ImageUtil.deleteImage(caminho);

        Prato prato = pratoRepository.getOne(id);
        prato.setStatus(0);
        pratoRepository.delete(prato);
    }

    public Prato serchById(Long id) {
        return pratoRepository.getOne(id);
    }
}
