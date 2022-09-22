<template>
	<svws-ui-content-card>
		<div class="entry-wrapper">
			<h2 class="svws-ui-text-black col-span-3">Beschäftigung</h2>
			<div class="flex flex-col">
				<div class="overflow-x-auto sm:-mx-6 lg:-mx-8">
					<div class="inline-block min-w-full py-2 sm:px-6 lg:px-8">
						<div class="overflow-hidden">
							<table class="min-w-full">
								<thead class="border-b bg-gray-100">
									<tr>
										<th v-for="(data, index) in headerTags" 
											:key="index"
											scope="col" class="px-6 py-4 text-left text-sm font-medium text-gray-900"
										>
											{{ data }}
										</th>
									</tr>
								</thead>

								<tbody>
								<tr v-for="(ds, id) in betriebe" :key="id"
								class="border-b bg-white transition duration-300 ease-in-out hover:bg-gray-400"
								@click="select(ds)"
							>	
									<s-card-schueler-beschaeftigungs-tabelle :betrieb="ds"/>
							</tr>	
						</tbody>
					</table>
				</div>
			</div>
		</div>

		
	</div>
		</div>
	</svws-ui-content-card>
</template>

<script setup lang="ts">
	import { computed, ComputedRef, defineAsyncComponent } from "vue";
	import { injectMainApp, Main } from "~/apps/Main";
	import { SchuelerBetriebsdaten } from "@svws-nrw/svws-core-ts";

	const SCardSchuelerBeschaeftigungsTabelle = defineAsyncComponent(
		() => import("~/components/schueler/adressen/SCardSchuelerBeschaeftigungTabelle.vue")
	);

	const main: Main = injectMainApp();
	const app = main.apps.schueler;

	const headerTags : ComputedRef<Array<string>> = computed(() => {
		return [ "Betrieb", "Ausbilder","Beschäftigungsart", "Beginn", "Ende", "Praktikum"];
	});
	
	const betriebe : ComputedRef<SchuelerBetriebsdaten[] | undefined> = computed(() => {
		return app.listSchuelerbetriebe?.liste;
	});

	function select(ds : SchuelerBetriebsdaten){
        if (app.listSchuelerbetriebe)
            app.listSchuelerbetriebe.ausgewaehlt = ds;
		
	}

</script>

<style scoped>
	.entry-content {
		@apply grid flex-grow grid-cols-1 gap-4 xl:grid-cols-2;
	}

	h2 {
		@apply mb-2 text-sm font-bold;
	}
</style>
