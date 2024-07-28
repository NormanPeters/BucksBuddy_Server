import {shallowMount, flushPromises} from '@vue/test-utils';
import JourneyCard from '../expenses/JourneyCard.vue';
import {describe, it, expect, vi} from 'vitest';
import type {AxiosResponse} from 'axios';
import type {Expenditure, Journey} from '../../types';
import api from "../../services/api";
import {AxiosHeaders} from 'axios';


describe('History', () => {
    const oneItemResponseExpenditures: Expenditure[] = [
        {id: 1, name: 'Hotel', amount: 100, date: new Date(), journeyId: 1}
    ];


    const oneItemResponseJourney: Journey[] = [{
        id: 1,
        name: 'Europe',
        startDate: new Date(),
        endDate: new Date(),
        homeCurr: 'EUR',
        vacCurr: 'USD',
        budget: 5000,
        travelDuration: '10'
    }];


    const mockAxiosResponseExpenditures = (data: Expenditure[]): AxiosResponse => ({
        data,
        status: 200,
        statusText: 'OK',
        config: {
            headers: new AxiosHeaders(),
        },
        headers: {},
    });


    const mockAxiosResponseJourney = (data: Journey[]): AxiosResponse => ({
        data,
        status: 200,
        statusText: 'OK',
        config: {
            headers: new AxiosHeaders(),
        },
        headers: {},
    });

    vi.mock('../../services/api');
    const mockedApi = vi.mocked(api, true);


    it('fetches expenditures when a journey is selected', async () => {
        localStorage.setItem('selectedJourneyId', '1');
        localStorage.setItem('uuid', '1');
        mockedApi.getAllExpenditures.mockResolvedValueOnce(mockAxiosResponseExpenditures(oneItemResponseExpenditures))
        mockedApi.getAllJourneys.mockResolvedValue({
            data: [{
                id: 1,
                name: 'Europe',
                startDate: new Date(),
                endDate: new Date(),
                homeCurr: 'EUR',
                vacCurr: 'USD',
                budget: 5000,
                travelDuration: '10'
            }],
            status: 200,
            statusText: 'OK',
            headers: {},
            config: {}
        } as AxiosResponse);  // Typisierung hinzufügen, um Typensicherheit zu gewährleisten
        const wrapper = shallowMount(JourneyCard);
        await flushPromises();



        const items = wrapper.findAll('.card-body');
        expect(items.length).toBe(1);
    });

    it('fetches journeys when a journey is selected', async () => {
        localStorage.setItem('selectedJourneyId', '1');
        localStorage.setItem('uuid', '1');
        mockedApi.getAllJourneys.mockResolvedValueOnce(mockAxiosResponseJourney(oneItemResponseJourney))
        const wrapper = shallowMount(JourneyCard);
        await flushPromises();

        const items = wrapper.findAll('.card-body');
        expect(items.length).toBe(1);
    });

    it('should render the journey card with the journey name', async () => {
        localStorage.setItem('selectedJourneyId', '1');
        localStorage.setItem('uuid', '1');
        mockedApi.getAllJourneys.mockResolvedValueOnce(mockAxiosResponseJourney(oneItemResponseJourney))
        const wrapper = shallowMount(JourneyCard);
        await flushPromises();

        expect(wrapper.text()).toContain('Europe');
    });

    // it('should render the journey card with the journey budget', async () => {
    //     localStorage.setItem('selectedJourneyId', '1');
    //     localStorage.setItem('uuid', '1');
    //     mockedApi.getAllJourneys.mockResolvedValueOnce(mockAxiosResponseJourney(oneItemResponseJourney))
    //     const wrapper = shallowMount(JourneyCard);
    //     await flushPromises();
    //
    //     mockedApi.getBudget.mockResolvedValueOnce({
    //         data: 5000,  // Direktes Setzen des Budgets
    //         status: 200,
    //         statusText: 'OK',
    //         headers: {},
    //         config: {}
    //     } as AxiosResponse);
    //
    //     expect(wrapper.text()).toContain('5000');
    // });

});


