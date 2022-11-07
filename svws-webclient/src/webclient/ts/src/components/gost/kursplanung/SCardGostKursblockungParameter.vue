
<template>
	<svws-ui-content-card v-if="app.dataKursblockung.daten" class="basis-1/3">
		<div class="flex flex-row">
			<div class="w-full flex-none sm:-mx-6 lg:-mx-8">
				<div class="py-2 align-middle sm:px-6 lg:px-8">
					<div class="overflow-hidden rounded-lg shadow">
						<Disclosure v-slot="{ open }">
							<DisclosureButton
								class="flex w-full justify-between rounded-lg font-bold bg-slate-100 px-4 py-2 text-left hover:bg-slate-200 focus:outline-none focus-visible:ring focus-visible:ring-slate-200 focus-visible:ring-opacity-75"
							>
								<span>Regeln zur Blockung</span>
								<i-ri-arrow-up-s-line
									:class="open ? 'rotate-180 transform' : ''"
									class="h-5 w-5"
								/>
							</DisclosureButton>
							<DisclosurePanel class="">
								<div class="py-1 px-8 bg-slate-100 shadow-lg rounded-lg mb-1">
									<Blockungsregel_1 />
								</div>
								<div class="py-1 px-8 bg-slate-100 shadow-lg rounded-lg my-1">
									<Blockungsregel_6 />
								</div>
								<div class="py-1 px-8 bg-slate-100 shadow-lg rounded-lg my-1">
									<Blockungsregel_2 />
								</div>
								<div class="py-1 px-8 bg-slate-100 shadow-lg rounded-lg my-1">
									<Blockungsregel_3 />
								</div>
								<div class="py-1 px-8 bg-slate-100 shadow-lg rounded-lg my-1">
									<Blockungsregel_4 />
								</div>
								<div class="py-1 px-8 bg-slate-100 shadow-lg rounded-lg my-1">
									<Blockungsregel_5 />
								</div>
							</DisclosurePanel>
						</Disclosure>
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
import { Disclosure, DisclosureButton, DisclosurePanel } from '@headlessui/vue'
import { injectMainApp, Main } from "~/apps/Main";

const main: Main = injectMainApp();
const app = main.apps.gost;
</script>
