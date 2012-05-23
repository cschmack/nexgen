# create project 
require 'fileutils'
require 'erb'
require File.join(File.dirname(__FILE__), '..', 'lib', 'help_printer')


HelpPrinter.new("create a project template").if_needs_help_print_and_exit()


puts "Creating project template..."



class CreateProj
  
  TEMPLATE_DIR='templates'
  DEFAULT_TEMPLATE = 'paas_service'



  def run
    check_current_dir
    proj_name = create_proj_dir
    puts "Creating project scaffolding using name: #{proj_name}"
    generate_from_template(proj_name, template_dir())
    initialize_repo
    puts "done!"
  end


  def current_dir
    File.dirname(File.expand_path(__FILE__))
  end

  def template_dir(template = DEFAULT_TEMPLATE)
    File.join(current_dir, '..', TEMPLATE_DIR, template)
  end
  
  
  def check_current_dir
    if (Dir.pwd == current_dir) || (Dir.pwd == File.dirname(current_dir))
      puts "Change to the target directory of the project you wish to create"
      exit
    end
  end

    
  protected
    def create_proj_dir
      proj_name = ARGV[1]

      if !proj_name.nil?
        puts "Creating project directory: #{proj_name}"
        FileUtils.mkdir(proj_name)
        Dir.chdir(proj_name)
      else
        # otherwise they just want to use the current directory.
        proj_name = File.basename(Dir.pwd)
      end
      @proj_name = proj_name
    end
  
    class Project
      attr_reader :proj_name
      def initialize(name)
        @proj_name = name
      end
      def get_binding
        binding
      end
    end
    
    def generate_from_template(proj_name, template_dir)
      proj = Project.new(proj_name)

      # copy the template files over
      generator_src = template_dir
      Dir.glob(File.join(generator_src, '**', '*')).each do |entry|
        relative_path_name = entry.sub(generator_src, '.').sub(/\/proj_name/,"/#{proj.proj_name}")
        if File.directory?(entry)
          FileUtils.mkdir_p(relative_path_name)
        elsif File.extname(entry) != '.erb'
          FileUtils.cp_r(entry, relative_path_name)
        else
          # process ERBs
          formatted = ERB.new(File.read(entry)).result(proj.get_binding)
          # strip off the .erb
          formatted_path_name = relative_path_name.sub('.erb', '')
          File.open(formatted_path_name, "w+") do |f|
            f.write(formatted)
          end
        end
      end
    end
    
    def initialize_repo
      # initialize git repo
      run "git init ."
    end
    
    def run(command)
      puts `#{command}`
    end
end














