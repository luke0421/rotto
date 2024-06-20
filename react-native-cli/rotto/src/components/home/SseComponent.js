import {useEffect, useRef, useState} from 'react';
import {DOMAIN_URL} from '@env';
import EventSource from 'react-native-sse';
import {Text, View} from 'react-native';
import TokenService from '../../utils/token';
import { setIsAlert } from '../../stores/alertSlice';
import { useDispatch } from 'react-redux';

const SseComponent = () => {
  const [message, setMessage] = useState('');
  const accessToken = useRef('');
  const dispatch = useDispatch();

  const getToken = async () => {
    accessToken.current = await TokenService.getAccessToken();
  };

  useEffect(() => {
    // SSE 연결
    const initializeSSE = async () => {
      await getToken();

      const url = new URL(DOMAIN_URL);
      const es = new EventSource(url + 'api/sse/connect', {
        headers: {
          Authorization: 'Bearer ' + accessToken.current,
        },
        withCredentials: true,

      });

      es.addEventListener('open', event => {
        // console.log('Open SSE connection.');
      });
      es.addEventListener('message', event => {
        // console.log('New message event:', event.data);
        setMessage(event.data); // 메시지를 상태에 설정
      });
      es.addEventListener('alert', event => {
        // console.log('alert:', event.data);
        setMessage(event.data);
      });
      es.addEventListener('subscription-1', event => {
        // console.log('청약 시작:', event.data);
        dispatch(setIsAlert(true))
        setMessage(event.data);
      });
      es.addEventListener('subscription-3', event => {
        // console.log('청약 시작:', event.data);
        dispatch(setIsAlert(true))
        setMessage(event.data);
      });
      es.addEventListener('subscription-7', event => {
        // console.log('청약 시작:', event.data);
        dispatch(setIsAlert(true))
        setMessage(event.data);
      });
      es.addEventListener('error', event => {
        if (event.type === 'error') {
          // console.error('Connection error:', event.message, es);
        } else if (event.type === 'exception') {
          // console.error('Error:', event.message, event.error);
        }
      });
      es.addEventListener('close', event => {
        // console.log('Close SSE connection.');
      });
    };

    initializeSSE();
  }, []);
};

export default SseComponent;
