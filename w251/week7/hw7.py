##USE PYTHON 2 

import object_storage
from timeit import Timer

# Initialize the authentication constants
user_id = 'SLOS1726245-2:SL1726245'
api_key = 'be50d25997da7f6065de2d22ea7bc8640c79936611ee34852a8d70f73540b944'
data_center = 'dal05'

# List to hold the runtimes
runtime_list = []

# Number of runs
number_of_runs = 5


# Create the storage object
sl_storage = object_storage.get_client(user_id,api_key , datacenter=data_center)

# To create a container with in the storage
def create_container(storage_obj, container_name):
    storage_obj[container_name].create()

# To load the file in the container
def load_file(storage_obj,container_name,file_location,dest_file):
    file_obj = open(file_location)
    storage_obj[container_name][dest_file].send(file_obj)

def list_objects(storage_obj,container_name):
    print storage_obj[container_name].objects()
    

def read_object(storage_obj,container_name,file_name):
    file_obj = storage_obj[container_name][file_name].read()
    

    
create_container(sl_storage,'Storage_Test')

# Writing file to the storage
print '\nTime to upload the file to the storage:\n'
for loop in range(number_of_runs):
    t = Timer(lambda: load_file(sl_storage, 'Storage_Test', '/root/data/largefile1','largefile1'))
    runtime = (t.timeit(number=1))
    print 'Attempt:' , loop +1 , ' is ', runtime , ' seconds.'
    runtime_list.append(runtime)
    
avg_time = sum(runtime_list) / float(len(runtime_list))
avg_speed = 1024 / avg_time
print 'Average time to upload the file to storage: {0:.2f}.'.format(avg_time) , ' seconds.'
print 'Average file upload speed in Mb/Sec: {0:.2f}.'.format(avg_speed)

runtime_list = []

# Reading file from the storage
print '\nTime to downlod the file from the storage:\n'
for loop in range(number_of_runs):
    t = Timer(lambda: read_object(sl_storage, 'Storage_Test', 'largefile1'))
    runtime = (t.timeit(number=1))
    print 'Attempt:' , loop +1 , ' is ', runtime , ' seconds.'
    runtime_list.append(runtime)
    
avg_time = sum(runtime_list) / float(len(runtime_list))
avg_speed = 1024 / avg_time
print 'Average time to download the file from storage: {0:.2f}.'.format(avg_time) , ' seconds.'
print 'Average file download speed in Mb/Sec: {0:.2f}.'.format(avg_speed)
