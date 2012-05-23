require 'getoptlong'

class HelpPrinter
  VERSION = "0.0.0.1"
  
  def initialize(tool_description = '')
    @tool_description = tool_description
  end
  
  def is_help_arg?()
    opts = GetoptLong.new(
      [ '--help', '-h', GetoptLong::OPTIONAL_ARGUMENT ]
    )
    opts.each do |opt, arg|
      case opt
      when '--help'
        return true
      end
    end
    false
  end
  
  def print(help_text = '') 
    puts %Q(
neutools Version: #{VERSION}  Neustar, Inc. (c) 2012
      
  neutools <tool name>

)
  
    if help_text.nil? || help_text == ''
      puts "  Tools available: "
      tools.each do |tool|
        puts "    #{tool}"
      end
    else
      puts help_text
    end
    puts ""
  end
  
  def if_needs_help_print_and_exit()
    if is_help_arg?
      tool = ''
      begin 
        raise ""
      rescue => e
        tool = File.basename(e.backtrace[1]).gsub(/\.rb.*/, '')
      end
      print "  #{tool} - #{@tool_description}"
      exit
    end
  end
  
  
  def tools
    tools_list = []
    Dir.glob("#{ARGV[0]}/*.rb").each do |tool|
      tool_name = File.basename(tool, '.rb')
      if tool_name != "tools_help"
        tools_list << "#{tool_name}"
      end
    end
    tools_list
  end
end

