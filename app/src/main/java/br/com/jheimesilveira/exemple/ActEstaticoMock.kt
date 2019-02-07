package br.com.jheimesilveira.exemple

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import br.com.jheimesilveira.js.filterview.dto.JSGroupFilterModel
import br.com.jheimesilveira.js.filterview.dto.JSItemFilterModel
import kotlinx.android.synthetic.main.act_estatico_mock.*

class ActEstaticoMock : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_estatico_mock)
        initFilterview()
    }

    private fun initFilterview() {

        val models = ArrayList<JSGroupFilterModel>()

        var model = JSGroupFilterModel("Ordenar por")
        model.multiple = false
        model.type = JSGroupFilterModel.Type.GRID
        model.dataSet = getDataSetOrderBy()
        models.add(model)

        model = JSGroupFilterModel("Tipo de produto")
        model.multiple = true
        model.type = JSGroupFilterModel.Type.LINEAR
        model.dataSet = getDataSetTypeProduct()
        models.add(model)

        model = JSGroupFilterModel("Gênero")
        model.multiple = true
        model.type = JSGroupFilterModel.Type.GRID
        model.dataSet = getDataSetGenre()
        models.add(model)

        model = JSGroupFilterModel("Tamanho")
        model.multiple = false
        model.type = JSGroupFilterModel.Type.LINEAR
        model.dataSet = getDataSetSize()
        models.add(model)

        /**
         * Loads all Filter Models
         * @param models Model
         */
        filterView.loadDataSetFilter(models)

        filterView.loadDataSetFilterSelected(
            // Index of the indexGroup to be Observed, where the index must be
            // greater than zero and less than the maximum value of groups
            indexGroup = 2
        ) {
            // In the function will be observed the indexGroup of filter options
            // in groupFilter and the indexGroup of filters already selected in
            // groupFilterSelected where you can edit the groupFilterSelected
            // object to add or remove filters in your dataSet
            groupFilter,
            groupFilterSelected ->

            //Returns to the function the new indexGroup of filters selected
            groupFilterSelected
        }

        /**
         * Any changes made to the selected objects this method will be called
         */
        filterView.onFinishListener { listDataSetFilter, listDataSetFilterSelected, dataSet ->
            filterView.tvHint.text = "${dataSet.size} Filtro(s) aplicado(s)"
        }
    }

    private fun getDataSetGenre(): ArrayList<JSItemFilterModel> {
        val model = ArrayList<JSItemFilterModel>()
        model.add(JSItemFilterModel("Mulher", 1))
        model.add(JSItemFilterModel("Homem", 2))
        model.add(JSItemFilterModel("Menino", 3))
        model.add(JSItemFilterModel("Menina", 4))

        return model
    }

    private fun getDataSetOrderBy(): ArrayList<JSItemFilterModel> {
        val model = ArrayList<JSItemFilterModel>()
        model.add(JSItemFilterModel("Menor preço", 1))
        model.add(JSItemFilterModel("Maior preço", 2))
        model.add(JSItemFilterModel("Oferta", 3))
        model.add(JSItemFilterModel("Mais vendidos", 4))
        model.add(JSItemFilterModel("Lançamanto", 5))

        return model
    }

    private fun getDataSetTypeProduct(): ArrayList<JSItemFilterModel> {
        val model = ArrayList<JSItemFilterModel>()
        model.add(JSItemFilterModel("Acessórios", 1))
        model.add(JSItemFilterModel("Bermudar", 2))
        model.add(JSItemFilterModel("Biquinis", 3))
        model.add(JSItemFilterModel("Calças", 4))
        model.add(JSItemFilterModel("Clinelo", 5))
        model.add(JSItemFilterModel("Meias", 6))
        model.add(JSItemFilterModel("Poupão", 8))
        model.add(JSItemFilterModel("Kits", 9))
        model.add(JSItemFilterModel("Óculos", 10))
        model.add(JSItemFilterModel("Natação", 11))
        model.add(JSItemFilterModel("Regatas", 12))
        model.add(JSItemFilterModel("Bermudas coloridas", 13))
        model.add(JSItemFilterModel("Saias", 14))
        model.add(JSItemFilterModel("Tenis", 15))
        model.add(JSItemFilterModel("Bonés", 16))

        return model
    }

    private fun getDataSetSize(): ArrayList<JSItemFilterModel> {
        val model = ArrayList<JSItemFilterModel>()
        model.add(JSItemFilterModel("GX", 1))
        model.add(JSItemFilterModel("GG", 2))
        model.add(JSItemFilterModel("G", 3))
        model.add(JSItemFilterModel("M", 4))
        model.add(JSItemFilterModel("N", 5))
        model.add(JSItemFilterModel("P", 6))

        return model
    }
}
