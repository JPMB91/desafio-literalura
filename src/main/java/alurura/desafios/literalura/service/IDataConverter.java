package alurura.desafios.literalura.service;

public interface IDataConverter {

    <T> T getData(String json, Class<T> tClass);

}
