package com.shmj.socketdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shahriar on 12/30/2017.
 */

public class WiFiDirectBroadcastReceiver extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;

    private List<WifiP2pDevice> mPeers = new ArrayList<WifiP2pDevice>();
    private List<WifiP2pConfig> mConfigs = new ArrayList<WifiP2pConfig>();

    public WiFiDirectBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel,
                                       MainActivity activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;


    }


    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        WifiP2pManager.PeerListListener myPeerListListener = null;

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {

            // Check to see if Wi-Fi is enabled and notify appropriate activity

            if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                    // Wifi P2P is enabled
                    Log.i("Wifi_P2P: ","Wifi P2P is enabled!");
                } else {
                    // Wi-Fi P2P is not enabled
                    Log.i("Wifi_P2P: ","Wifi P2P is not enabled!");
                }
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
            /*if (mManager != null) {
                mManager.requestPeers(mChannel, myPeerListListener );
            }*/

            Log.d("wifi", "WIFI PEERS CHANGED");
            mPeers = new ArrayList<WifiP2pDevice>();
            mConfigs = new ArrayList<WifiP2pConfig>();
            if(mManager != null){
                WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener(){
                    @Override
                    public void onPeersAvailable(WifiP2pDeviceList peerList) {
                        mPeers.clear();
                        Log.i("Peers cleared : ",String.valueOf(mPeers));
                        mPeers.addAll(peerList.getDeviceList());
                        Log.i("Peers : ",String.valueOf(mPeers));

                        mActivity.displayPeers(peerList);
                        MainActivity.textView.setText("peers : "+ String.valueOf(mPeers));
                        //mPeers.addAll(peerList.getDeviceList());
                        if (mPeers.size() == 0) {
                            Log.i("Peers List : ", "No devices found");
                            return;
                        }
                        for(int i=0; i < peerList.getDeviceList().size(); i++){
                            WifiP2pConfig config = new WifiP2pConfig();
                            config.deviceAddress = mPeers.get(i).deviceAddress;
                            mConfigs.add(config);
                        }

                        //connect to desired device from the peers list
                        WifiP2pDevice device = mPeers.get(0);
                        WifiP2pConfig config = new WifiP2pConfig();
                        config.deviceAddress = device.deviceAddress;
                        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {

                            @Override
                            public void onSuccess() {
                                //success logic
                                Log.i("conecttion to device: ", "Success!");
                            }

                            @Override
                            public void onFailure(int reason) {
                                //failure logic
                                Log.i("conecttion to device: ", "failure!");
                            }
                        });



                    }
                };
                mManager.requestPeers(mChannel, peerListListener);
            }


        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }


    }
}
