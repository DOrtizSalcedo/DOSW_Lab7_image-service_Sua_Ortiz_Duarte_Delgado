package edu.eci.dosw.imageservice.controller;

import edu.eci.dosw.imageservice.model.document.ImagenDocument;
import edu.eci.dosw.imageservice.service.ImagenService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/imagenes")
public class ImagenController {

    private final ImagenService imagenService;

    public ImagenController(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImagenDocument> subirImagen(@RequestParam("archivo") MultipartFile archivo,
                                                      @RequestParam("referenciaExterna") String referenciaExterna)
            throws IOException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(imagenService.guardar(archivo, referenciaExterna));
    }

    @GetMapping
    public ResponseEntity<List<ImagenDocument>> listar() {
        return ResponseEntity.ok(imagenService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable String id) {
        ImagenDocument imagen = imagenService.buscarPorId(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + imagen.getNombre() + "\"")
                .contentType(MediaType.parseMediaType(imagen.getTipoContenido()))
                .body(imagen.getDatos());
    }

    @GetMapping("/referencia/{referenciaExterna}")
    public ResponseEntity<List<ImagenDocument>> listarPorReferencia(@PathVariable String referenciaExterna) {
        return ResponseEntity.ok(imagenService.listarPorReferencia(referenciaExterna));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        imagenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}