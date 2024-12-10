package com.example.demo;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;



import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin(origins = "https://workshopmanagement.vercel.app")
public class AppController {
	@Autowired
	DAO dao;
	
	@GetMapping("/")
	public String fun1() {
		return "Welcome";
	}
	
	@PostMapping("/register")
	public String fun2(@RequestBody User user) {
		if(dao.findUser(user.getEmail())==null) {
			dao.insert(user);
			return "User Inserted";
		}else {
			return "User Already Exists";
		}
	}
	
//	@PostMapping("/login")
//	public ResponseEntity<?> login(@RequestBody User user) {
//	    User foundUser = dao.findUser(user.getEmail());
//	    if (foundUser != null) {
//	        if (foundUser.getPassword().equals(user.getPassword())) {
//	            return ResponseEntity.ok(foundUser);
//	        } else {
//	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
//	        }
//	    }
//	    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
//	}
	
	
	
//	@PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody User user) {
//        String captcha = user.getCaptcha(); // Retrieve captcha value from the request
//        String url = "https://www.google.com/recaptcha/api/siteverify";
//        String secretKey = "6Lfd7ZQqAAAAAL2_jC0kHY1cqqphCZZNX-Dvk8LZ"; // Replace with your reCAPTCHA secret key
//        String params = "?secret=" + secretKey + "&response=" + captcha;
//
//        RestTemplate restTemplate = new RestTemplate();
//        String verificationResponse = restTemplate.getForObject(url + params, String.class);
//
//        JSONObject json = new JSONObject(verificationResponse);
//        boolean success = json.getBoolean("success");
//
//        if (!success) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("CAPTCHA verification failed");
//        }
//
//        // Authenticate user after CAPTCHA verification
//        User foundUser = dao.findUser(user.getEmail());
//        if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
//            return ResponseEntity.ok(foundUser);
//        } else if (foundUser == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
//        }
//    }
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody User user) {
	    String captcha = user.getCaptcha();
	    String url = "https://www.google.com/recaptcha/api/siteverify";
	    String secretKey = "6Lfd7ZQqAAAAAL2_jC0kHY1cqqphCZZNX-Dvk8LZ"; // Replace with your actual secret key
	    String params = "?secret=" + secretKey + "&response=" + captcha;

	    RestTemplate restTemplate = new RestTemplate();
	    String verificationResponse = restTemplate.getForObject(url + params, String.class);

	    // Debugging: Log the response from reCAPTCHA API
	    System.out.println("reCAPTCHA Verification Response: " + verificationResponse);

	    JSONObject json = new JSONObject(verificationResponse);
	    boolean success = json.getBoolean("success");

	    if (!success) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("CAPTCHA verification failed");
	    }

	    // Continue with user authentication
	    User foundUser = dao.findUser(user.getEmail());
	    if (foundUser != null && foundUser.getPassword().equals(user.getPassword())) {
	        return ResponseEntity.ok(foundUser);
	    } else if (foundUser == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
	    } else {
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password.");
	    }
	}

	
	
	
	@Autowired
	private final WorkshopInterface workshopRepository;

    public AppController(WorkshopInterface workshopRepository) {
        this.workshopRepository = workshopRepository;
    }
	
    @GetMapping("/workshop/{id}")
    public Workshop getWorkshopById(@PathVariable Long id) {
        return workshopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workshop not found with id: " + id));
    }

	
	@GetMapping("/workshops")
    public List<Workshop> getAllWorkshops() {
        return workshopRepository.findAll();
    }
	
//	@PostMapping("/createworkshops")
//	ublic ResponseEntity<Workshop> createWorkshop(@RequestBody Workshop workshop) {
//	    Workshop savedWorkshop = workshopRepository.save(workshop);
//	    return ResponseEntity.ok(savedWorkshop);
//	}
	private static final String UPLOAD_DIR = "uploads/";
	@PostMapping("/createworkshops")
    public ResponseEntity<Workshop> createWorkshop(
            @RequestParam("title") String title,
            @RequestParam("company") String Company,
            @RequestParam("instructor") String instructor,
            @RequestParam("description") String description,
            @RequestParam("benefits") String benefits,
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            @RequestParam("availableSlots") int availableSlots,
            @RequestParam("image") MultipartFile imageFile) {

        Workshop workshop = new Workshop();
        workshop.setTitle(title);
        workshop.setCompany(Company);
        workshop.setInstructor(instructor);
        workshop.setDescription(description);
        workshop.setBenefits(benefits);
        workshop.setDate(date);
        workshop.setTime(time);
        workshop.setAvailableSlots(availableSlots);

        // Save the image file
        if (!imageFile.isEmpty()) {
            try {
                String imagePath = UPLOAD_DIR + imageFile.getOriginalFilename();
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                Path path = Paths.get(imagePath);
                Files.write(path, imageFile.getBytes());
                workshop.setImage("https://wmsserver-production.up.railway.app/uploads/" + imageFile.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        Workshop savedWorkshop = workshopRepository.save(workshop);
        return ResponseEntity.ok(savedWorkshop);
    }
	
	
	
	@PutMapping("/workshops/{id}")
    public ResponseEntity<Workshop> updateWorkshop(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("company") String company,
            @RequestParam("instructor") String instructor,
            @RequestParam("description") String description,
            @RequestParam("benefits") String benefits,
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            @RequestParam("availableSlots") int availableSlots,
            @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        // Check if the workshop exists
        Workshop workshop = workshopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Workshop not found with id: " + id));

        // Update the workshop details
        workshop.setTitle(title);
        workshop.setCompany(company);
        workshop.setInstructor(instructor);
        workshop.setDescription(description);
        workshop.setBenefits(benefits);
        workshop.setDate(date);
        workshop.setTime(time);
        workshop.setAvailableSlots(availableSlots);

        // Update image if a new one is provided
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imagePath = UPLOAD_DIR + imageFile.getOriginalFilename();
                File uploadDir = new File(UPLOAD_DIR);
                if (!uploadDir.exists()) uploadDir.mkdirs();
                Path path = Paths.get(imagePath);
                Files.write(path, imageFile.getBytes());
                workshop.setImage("http://localhost:8081/uploads/" + imageFile.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        // Save the updated workshop
        Workshop updatedWorkshop = workshopRepository.save(workshop);
        return ResponseEntity.ok(updatedWorkshop);
    }
	
	
	
	
	
	@DeleteMapping("/workshops/{id}")
    public ResponseEntity<Void> deleteWorkshop(@PathVariable Long id) {
        if (workshopRepository.existsById(id)) {
            workshopRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
	@GetMapping("/workshops/{workshopId}/registrations")
    public ResponseEntity<List<User>> getRegisteredUsers(@PathVariable Long workshopId) {
        List<User> registeredUsers = workshopRepository.findUsersByWorkshopId(workshopId);
        return ResponseEntity.ok(registeredUsers);
    }
	
	
	@Autowired
	private  UserInterface userRepository;
	@Autowired
	private  RegistrationInterface registrationRepository;
//	@PostMapping("/registerworkshop/{workshopId}/register/{userId}")
//	public ResponseEntity<?> registerForWorkshop(@PathVariable Long workshopId, @PathVariable Long userId) {
//	    // Check if the workshop and user exist
//	    Workshop workshop = workshopRepository.findById(workshopId).orElse(null);
//	    User user = userRepository.findById(userId).orElse(null);
//
//	    if (workshop == null || user == null) {
//	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Workshop not found.");
//	    }
//
//	    // Create a registration entry in the registration table
//	    Registration registration = new Registration();
//	    registration.setUserId(userId);
//	    registration.setWorkshopId(workshopId);
//
//	    registrationRepository.save(registration);  // Save the registration to the database
//
//	    return ResponseEntity.ok("User successfully registered for the workshop");
//	}

	@PostMapping("/registerworkshop/{workshopId}/register/{userId}")
	public ResponseEntity<?> registerForWorkshop(@PathVariable Long workshopId, @PathVariable Long userId) {
	    // Check if the workshop and user exist
	    Workshop workshop = workshopRepository.findById(workshopId).orElse(null);
	    User user = userRepository.findById(userId).orElse(null);

	    if (workshop == null || user == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or Workshop not found.");
	    }

	    // Check if the user is already registered for the workshop
	    boolean isAlreadyRegistered = registrationRepository.existsByUserIdAndWorkshopId(userId, workshopId);

	    if (isAlreadyRegistered) {
	        return ResponseEntity.status(HttpStatus.CONFLICT).body("User is already registered for this workshop.");
	    }

	    // Create a registration entry in the registration table
	    Registration registration = new Registration();
	    registration.setUserId(userId);
	    registration.setWorkshopId(workshopId);

	    registrationRepository.save(registration);  // Save the registration to the database

	    return ResponseEntity.ok("User successfully registered for the workshop");
	}

	
	
	// Controller method to get registered workshops for a user
	@GetMapping("/registeredworkshops/{userId}")
	public List<Workshop> getRegisteredWorkshops(@PathVariable Long userId) {
	    return registrationRepository.findById(userId).stream()
	            .map(registration -> workshopRepository.findById(registration.getWorkshopId()).orElse(null))
	            .collect(Collectors.toList());
	}

	
}
