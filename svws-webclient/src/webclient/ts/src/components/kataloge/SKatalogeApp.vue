<template>
	<div class="flex h-full flex-row">
		<div class="flex w-full flex-col px-4">
			<svws-ui-header><span></span></svws-ui-header>
			<svws-ui-tab-bar v-model="app.selectedTab">
				<template #tabs>
					<svws-ui-tab-button>Daten</svws-ui-tab-button>
				</template>
				<template #panels>
					<svws-ui-tab-panel>
					</svws-ui-tab-panel>
				</template>
			</svws-ui-tab-bar>
		</div>
	</div>
</template>

<script setup lang="ts"></script>
