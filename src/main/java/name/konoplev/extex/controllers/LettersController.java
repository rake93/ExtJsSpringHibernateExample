package name.konoplev.extex.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import name.konoplev.extex.dao.LetterDao;
import name.konoplev.extex.domain.entities.Letter;
import name.konoplev.extex.forms.LetterForm;
import name.konoplev.extex.json.ErrorJSON;
import name.konoplev.extex.json.FormResponseJSON;
import name.konoplev.extex.json.LetterJSON;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class LettersController implements HandlerExceptionResolver {
	private static final Logger LOG = Logger.getLogger(LettersController.class
			.toString());

	@Autowired
	private LetterDao letterDao;
	@Autowired
	private MessageSource messageSource;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String root(Locale locale, Model model) {
		return "letters";
	}

	@RequestMapping(value = "/add.json", method = RequestMethod.POST)
	@ResponseBody
	public FormResponseJSON add(
			@ModelAttribute("letterForm") @Valid LetterForm letterForm,
			BindingResult result, Locale locale, Model model) {
		FormResponseJSON responseJSON = new FormResponseJSON();

		if (!result.hasErrors()) {
			Letter letter = new Letter();
			letter.setDocNumber(letterForm.getDocNumber());
			letter.setTitle(letterForm.getTitle());
			letter.setTextContent(letterForm.getTextContent());
			letter.setCreationDate(letterForm.getCreationDate());
			letter.setPublished(letterForm.isPublished());
			letter.setAttachedFile(letterForm.getAttachedFile().getBytes());

			try {
				letterDao.saveLetter(letter);
				responseJSON.setMessage(messageSource.getMessage(
						"form.result.ok", null, locale));
			} catch (Exception e) {
				responseJSON.setSuccess(false);
				responseJSON.setMessage(messageSource.getMessage(
						"form.result.errors.base", null, locale));
			}
		} else {
			responseJSON.setSuccess(false);
			responseJSON.setMessage(messageSource.getMessage(
					"form.result.errors", null, locale));
			responseJSON.setErrors(new ArrayList<ErrorJSON>());
			List<FieldError> errors = result.getFieldErrors();
			for (FieldError error : errors) {
				List<String> types = Arrays.asList(error.getCodes());
				if (0 < types.size()) {
					Object[] args = error.getArguments();
					responseJSON.getErrors().add(
							new ErrorJSON(error.getField(), messageSource
									.getMessage(types.get(0), args, locale)));
				}
			}
		}

		return responseJSON;
	}

	@RequestMapping(value = "list.json", method = RequestMethod.GET)
	@ResponseBody
	public List<LetterJSON> list() {
		List<Letter> letters = letterDao.getAllLetters();
		List<LetterJSON> letterJSONs = new ArrayList<LetterJSON>();
		for (Letter letter : letters) {
			letterJSONs.add(new LetterJSON(letter));
		}
		return letterJSONs;
	}

	@RequestMapping(value = "publish.json", method = RequestMethod.GET)
	@ResponseBody
	public Boolean publish(HttpServletRequest request,
			HttpServletResponse response) {
		String lid = request.getParameter("lid");
		if (null != lid) {
			Integer letterId = Integer.parseInt(lid);
			Letter letter = letterDao.getLetterById(letterId);
			if (null != letter) {
				letter.setPublished(true);
				letterDao.updateLetter(letter);
				return true;
			}
		}

		return false;
	}

	@RequestMapping(value = "file/{id}", method = RequestMethod.GET)
	public String downloadFile(@PathVariable(value = "id") Integer letterId,
			Locale locale, Model model, HttpServletResponse response) {
		Letter letter = letterDao.getLetterById(letterId);
		if (null != letter) {
			response.setHeader("Content-Disposition", "inline; filename=\"file"
					+ letterId + "\"");
			String contentType = "application/octet-stream";
			try {
				OutputStream out = response.getOutputStream();
				response.setContentType(contentType);
				out.write(letter.getAttachedFile());
				out.flush();
				out.close();
			} catch (IOException e) {
				throw new RuntimeException(e.getLocalizedMessage());
			}
		}

		return null;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		if (ex instanceof MaxUploadSizeExceededException) {
			response.setContentType("text/html");
			response.setStatus(HttpStatus.REQUEST_ENTITY_TOO_LARGE.value());

			try {
				PrintWriter out = response.getWriter();
				Long maxSizeInBytes = ((MaxUploadSizeExceededException) ex)
						.getMaxUploadSize();

				FormResponseJSON responseJSON = new FormResponseJSON();
				responseJSON.setSuccess(false);
				responseJSON.setMessage(messageSource.getMessage(
						"Error.page.letters.unexpected", null,
						request.getLocale()));
				responseJSON.setErrors(new ArrayList<ErrorJSON>());
				responseJSON.getErrors().add(
						new ErrorJSON("attachedFile", messageSource.getMessage(
								"Error.page.letters.maxfilesize",
								new Object[] { maxSizeInBytes },
								request.getLocale())));

				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(responseJSON);

				LOG.info(json);

				out.println(json);

				out.close();
			} catch (IOException e) {
				LOG.severe("Error writing to output stream");
			}
		}

		return null;
	}
}