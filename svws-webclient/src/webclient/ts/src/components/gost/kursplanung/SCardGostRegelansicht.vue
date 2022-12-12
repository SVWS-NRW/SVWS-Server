
<template>
	<svws-ui-content-card v-if="app.dataKursblockung.daten" class="">
		<div class="flex flex-row">
			<div class="w-full flex-none">
				<div class="py-2 align-middle sm:px-6 lg:px-8">
					<div class="overflow-hidden rounded-lg shadow">
								<span class="font-bold ">Regeln zur Blockung</span>
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
									<Blockungsregel_7 />
								</div>
								<div class="py-1 px-8 bg-slate-100 shadow-lg rounded-lg my-1">
									<Blockungsregel_8 />
								</div>
								<div class="py-1 px-8 bg-slate-100 shadow-lg rounded-lg my-1">
									<Blockungsregel_4 />
								</div>
								<div class="py-1 px-8 bg-slate-100 shadow-lg rounded-lg my-1">
									<Blockungsregel_5 />
								</div>
					</div>
				</div>
			</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
import { injectMainApp, Main } from "~/apps/Main";

const main: Main = injectMainApp();
const app = main.apps.gost;
</script>
