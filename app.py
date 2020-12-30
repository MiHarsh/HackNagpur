from __future__ import division, print_function
# coding=utf-8
import sys
import os
import glob
import re
import numpy as np
import torch
from flask import jsonify
# from six.moves import urllib
import urllib.request
from PIL import Image
import albumentations as aug
from efficientnet_pytorch import EfficientNet

# Flask utils
from flask import Flask, redirect, url_for, request, render_template, jsonify
from werkzeug.utils import secure_filename
from gevent.pywsgi import WSGIServer

# Define a flask app
app = Flask(__name__)

labels = np.load('labels.npy',allow_pickle=True)[0]

# Lets load all the models here, to avoid reloading model
model_canc = torch.load("canc.pth")
model_canc.eval()

model_breast = torch.load("BreastCancer.pth")
model_breast.eval()

model_alzheimer = torch.load("Alzheimer.pth")
model_alzheimer.eval()

model_aptos = torch.load("Aptos.pth")
model_aptos.eval()

model_tumor = torch.load("Braintumor.pth")
model_tumor.eval()

model_covid = torch.load("Covid.pth")
model_covid.eval()

model_pneumonia = torch.load("Pneumonia.pth")
model_pneumonia.eval()



def model_predict(file, model):
    image = Image.open(file)
    image = np.array(image)
    transforms = aug.Compose([
            aug.Resize(224,224),
            aug.Normalize((0.485, 0.456, 0.406),(0.229, 0.224, 0.225),max_pixel_value=255.0,always_apply=True),
            ])
    image = transforms(image=image)["image"]
    image = np.transpose(image, (2, 0, 1)).astype(np.float32)
    image = torch.tensor([image], dtype=torch.float)
    preds = model(image)
    probs  = preds.detach().numpy()[0]
    probs = np.exp(probs)/np.sum(np.exp(probs))
    return probs

@app.route('/')
def home():
    # Main page
    return render_template('home.html')


@app.route('/imageUpload',methods=['GET'])
def index():
    # image upload page
    return render_template('index.html')

@app.route('/covid',methods=['GET'])
def covid():
    # image upload page
    return render_template('covid.html')

@app.route('/bt',methods=['GET'])
def bt():
    # image upload page
    return render_template('bt.html')

@app.route('/aptos',methods=['GET'])
def aptos():
    # image upload page
    return render_template('aptos.html')

@app.route('/breastcancer',methods=['GET'])
def breastcancer():
    # image upload page
    return render_template('breastcancer.html')

@app.route('/pneumonia',methods=['GET'])
def pneumonia():
    # image upload page
    return render_template('pneumonia.html')

@app.route('/alzymer',methods=['GET'])
def alzymer():
    # image upload page
    return render_template('alzymer.html')


@app.route('/profile',methods=['GET'])
def profile():
    # image upload page
    return render_template('profile.html')

# Define Predict Function ---->>>>>>

@app.route('/alzymer/predict', methods=['POST'])
def upload_alzymer():
    # Get the file from post request
    f = request.files['file']
    # Make prediction
    probs = model_predict(f, model_alzheimer)
    labs = labels['alzheimer']
    probs = ["%.4f" % x for x in probs]
    outs = {}
    for i in range(len(labs)):
        outs[labs[i]]=probs[i]
    return outs

@app.route('/alzymer/predict1', methods=['POST'])
def upload_alzymer1():
    # Get the file from post request
    if request.method =='POST':
        skin_lesion=request.get_json()
        image_url=skin_lesion['url']
        urllib.request.urlretrieve(image_url, "sample.png")
        # Make prediction
        probs = model_predict("sample.png", model_alzheimer)
        labs = labels['alzheimer']
        probs = ["%.4f" % x for x in probs]
        outs = {}
        for i in range(len(labs)):
            outs[labs[i]]=probs[i]
    return outs
    
@app.route('/pneumonia/predict', methods=['POST'])
def upload_pneumonia():
    # Get the file from post request
    f = request.files['file']
    # Make prediction
    probs = model_predict(f, model_pneumonia)
    labs = labels['pneumonia']           
    probs = ["%.4f" % x for x in probs]
    outs = {}
    for i in range(len(labs)):
        outs[labs[i]]=probs[i]
    return outs
 
@app.route('/pneumonia/predict1', methods=['POST'])
def upload_pneumonia1():
    # Get the file from post request
    if request.method =='POST':
        skin_lesion=request.get_json()
        image_url=skin_lesion['url']
        urllib.request.urlretrieve(image_url, "sample.png")
        # Make prediction
        probs = model_predict("sample.png", model_pneumonia)
        labs = labels['pneumonia']
        probs = ["%.4f" % x for x in probs]
        outs = {}
        for i in range(len(labs)):
            outs[labs[i]]=probs[i]
    return outs

@app.route('/aptos/predict', methods=['POST'])
def upload_aptos():
    # Get the file from post request
    f = request.files['file']
    # Make prediction
    probs = model_predict(f, model_aptos)
    labs = labels['aptos']           
    probs = ["%.4f" % x for x in probs]
    outs = {}
    for i in range(len(labs)):
        outs[labs[i]]=probs[i]
    return outs
 
@app.route('/aptos/predict1', methods=['POST'])
def upload_aptos1():
    # Get the file from post request
    if request.method =='POST':
        skin_lesion=request.get_json()
        image_url=skin_lesion['url']
        urllib.request.urlretrieve(image_url, "sample.png")
        # Make prediction
        probs = model_predict("sample.png", model_aptos)
        labs = labels['aptos']
        probs = ["%.4f" % x for x in probs]
        outs = {}
        for i in range(len(labs)):
            outs[labs[i]]=probs[i]
    return outs

@app.route('/braintumor/predict', methods=['POST'])
def upload_braintumor():
    # Get the file from post request
    f = request.files['file']
    # Make prediction
    probs = model_predict(f, model_tumor)
    labs = labels['braintumor']        
    probs = ["%.4f" % x for x in probs]
    outs = {}
    for i in range(len(labs)):
        outs[labs[i]]=probs[i]
    return outs

@app.route('/braintumor/predict1', methods=['POST'])
def upload_braintumor1():
    # Get the file from post request
    if request.method =='POST':
        skin_lesion=request.get_json()
        image_url=skin_lesion['url']
        urllib.request.urlretrieve(image_url, "sample.png")
        # Make prediction
        probs = model_predict("sample.png", model_tumor)
        labs = labels['braintumor']
        probs = ["%.4f" % x for x in probs]
        outs = {}
        for i in range(len(labs)):
            outs[labs[i]]=probs[i]
    return outs

@app.route('/skincancer/predict', methods=['POST'])
def upload_skincancer():
    # Get the file from post request
    f = request.files['file']
    # Make prediction
    probs = model_predict(f, model_canc)
    labs = labels['skincancer']      
    probs = ["%.4f" % x for x in probs]
    outs = {}
    for i in range(len(labs)):
        outs[labs[i]]=probs[i]
    return outs

@app.route('/skincancer/predict1', methods=['POST'])
def upload_skincancer1():
    # Get the file from post request
    if request.method =='POST':
        skin_lesion=request.get_json()
        image_url=skin_lesion['url']
        urllib.request.urlretrieve(image_url, "sample.png")
        # Make prediction
        probs = model_predict("sample.png", model_canc)
        labs = labels['skincancer']
        probs = ["%.4f" % x for x in probs]
        outs = {}
        for i in range(len(labs)):
            outs[labs[i]]=probs[i]
    return outs

@app.route('/covid/predict', methods=['POST'])
def upload_covid():
    # Get the file from post request
    f = request.files['file']
    # Make prediction
    probs = model_predict(f, model_covid)
    labs = labels['covid']          
    probs = ["%.4f" % x for x in probs]
    outs = {}
    for i in range(len(labs)):
        outs[labs[i]]=probs[i]
    return outs

@app.route('/covid/predict1', methods=['POST'])
def upload_covid1():
    # Get the file from post request
    if request.method =='POST':
        skin_lesion=request.get_json()
        image_url=skin_lesion['url']
        urllib.request.urlretrieve(image_url, "sample.png")
        # Make prediction
        probs = model_predict("sample.png", model_covid)
        labs = labels['covid']
        probs = ["%.4f" % x for x in probs]
        outs = {}
        for i in range(len(labs)):
            outs[labs[i]]=probs[i]
    return outs

@app.route('/breastcancer/predict', methods=['POST'])
def upload_breastcancer():
    # Get the file from post request
    f = request.files['file']
    # Make prediction
    probs = model_predict(f, model_breast)
    labs = labels['breast']       
    probs = ["%.4f" % x for x in probs]
    outs = {}
    for i in range(len(labs)):
        outs[labs[i]]=probs[i]
    return outs

@app.route('/breastcancer/predict1', methods=['POST'])
def upload_breastcancer1():
    # Get the file from post request
    if request.method =='POST':
        skin_lesion=request.get_json()
        image_url=skin_lesion['url']
        urllib.request.urlretrieve(image_url, "sample.png")
        # Make prediction
        probs = model_predict("sample.png", model_breast)
        labs = labels['breast']
        probs = ["%.4f" % x for x in probs]
        outs = {}
        for i in range(len(labs)):
            outs[labs[i]]=probs[i]
    return outs

if __name__ == '__main__':
    app.run(debug=True)
